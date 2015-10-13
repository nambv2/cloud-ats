/**
 * 
 */
package org.ats.services.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.ats.common.MapBuilder;
import org.ats.common.StringUtil;
import org.ats.common.http.HttpURL;
import org.ats.services.keyword.KeywordProjectService;
import org.ats.services.keyword.Suite;
import org.ats.services.keyword.SuiteReference;
import org.ats.services.performance.JMeterArgument;
import org.ats.services.performance.JMeterSampler;
import org.ats.services.performance.JMeterScript;
import org.ats.services.performance.JMeterScriptReference;
import org.ats.services.performance.PerformanceProjectService;
import org.rythmengine.RythmEngine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Jul 14, 2015
 */
@Singleton
public class GeneratorService {

  @Inject
  KeywordProjectService kpService;
  
  @Inject
  PerformanceProjectService perService;
  
  /**
   * 
   * @param outDir
   * @param project
   * @param compress
   * @param scripts 
   * @return The path to the project located
   * @throws IOException
   */
  public String generatePerformance(String outDir, String jobId, boolean compress, List<JMeterScriptReference> scripts) throws IOException {
    File sourceDir = new File(outDir + "/" + jobId  + "/src/test/java/org/ats/generated");
    sourceDir.mkdirs();
    
    File resourceDir = new File(outDir + "/" + jobId  + "/src/test/resources/jmeter/bin");
    resourceDir.mkdirs();
    
    loadJMeterProperties(resourceDir);
    loadJMeterUtilities(sourceDir);
    loadJMeterPOM(outDir + "/" + jobId);
    
    String runnerTemplate = StringUtil.readStream(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/JMeterRunner.java.tmpl"));
    
    String scriptTemplate = StringUtil.readStream(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/script.tmpl"));
    
    String samplerTemplate = StringUtil.readStream(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/sampler.tmpl"));

    RythmEngine engine = new RythmEngine(new MapBuilder<String, Boolean>("codegen.compact", false).build());
    
    StringBuilder scriptBuilder = new StringBuilder();
    
    Set<String> namePool = new HashSet<String>();
    
    for (JMeterScriptReference ref : scripts) {
      
      if (ref.get().getProjectId() == null) continue;
      
      JMeterScript jScript = ref.get();
      String scriptName = getAvailableName(StringUtil.normalizeName(jScript.getName()), namePool);
      int loops = jScript.getLoops();
      int numberThreads = jScript.getNumberThreads();
      int ramUp = jScript.getRamUp();
      
      StringBuilder samplerBuilder = new StringBuilder();
      
      for (JMeterSampler jSampler :jScript.getSamplers()) {
        String samplerName = jSampler.getName();
        String defaultTimeout = "120000";
        HttpURL url = new HttpURL(jSampler.getUrl());
        String domain = url.getDomain();
        if (domain.indexOf(':') != -1) {
          domain = domain.substring(0, domain.indexOf(':'));
        }
        
        int port = url.getPort();
        String protocol = url.getProtocol();
        String path = url.getFullPath();
        String method = jSampler.getMethod().toString();
        
        StringBuilder paramBuilder = new StringBuilder("new MapBuilder()");
        
        for (JMeterArgument jArg : jSampler.getArguments()) {
          paramBuilder.append(".append(\"").append(jArg.getParamName()).append("\",\"").append(jArg.getParamValue()).append("\")");
        }
        paramBuilder.append(".build()");
        String params = paramBuilder.toString();
        
        String assertionText = jSampler.getAssertionText();
        
        if("null".equals(assertionText)) assertionText = "";
        
        String constantTimer = Long.toString(jSampler.getConstantTime());
        
        if ("null".equals(constantTimer)) constantTimer = "";
        
        samplerBuilder.append(engine.render(samplerTemplate, samplerName, defaultTimeout, domain, port, protocol, path, method, params, assertionText, constantTimer, numberThreads));
      }
      
      String samplers = samplerBuilder.toString();
      scriptBuilder.append(engine.render(scriptTemplate, ref.getId(), scriptName, loops, numberThreads, ramUp, samplers));
    }
    
    Map<String, String> params = new HashMap<String, String>();
    params.put("scripts", scriptBuilder.toString());
    String runner = engine.render(runnerTemplate, params);
    
    FileOutputStream os = new FileOutputStream(new File(sourceDir, "JMeterRunner.java"));
    os.write(runner.getBytes());
    os.flush();
    os.close();
    
    if (compress) {
      compress(jobId, outDir + "/" + jobId, outDir + "/" + jobId + ".zip");
      return outDir + "/" + jobId + ".zip";
    }
    
    return outDir + "/" + jobId;
  }
  
  /**
   * 
   * @param outDir
   * @param project
   * @param compress
   * @return The path to project located
   * @throws IOException
   */
  
  public String generateKeyword(String outDir, String jobId, boolean compress, List<SuiteReference> suites, boolean showAction, int valueDelay) throws IOException{
    File sourceDir = new File(outDir + "/" + jobId  + "/src/test/java/org/ats/generated");
    sourceDir.mkdirs();
    
    loadKeywordPOM(outDir + "/" + jobId);

    Set<String> pool = new HashSet<String>();
    
    for (SuiteReference suiteRef : suites) {
      
      Suite suite = suiteRef.get();
      String fileName = getAvailableName(StringUtil.normalizeName(suite.getName()), pool) + ".java";
      FileOutputStream os = new FileOutputStream(new File(sourceDir, fileName));
      os.write(suite.transform(showAction,valueDelay).getBytes());
      os.flush();
      os.close();
    }
    
    if (compress) {
      compress(jobId, outDir + "/" + jobId,  outDir + "/" + jobId  + ".zip");
      return outDir + "/" + jobId  + ".zip";
    }
    
    return outDir + "/" + jobId;
  }
  
  public String generateKeyword(String outDir, String jobId, boolean compress, List<SuiteReference> suites) throws IOException {
    return generateKeyword(outDir,jobId,compress,suites,false,0);
  }
  
  private void loadKeywordPOM(String outDir) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("keyword/pom.xml");
    write(is, new FileOutputStream(new File(outDir, "pom.xml")));
  }
  
  private void loadJMeterPOM(String outDir) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/pom.xml");
    write(is, new FileOutputStream(new File(outDir, "pom.xml")));
  }
  
  private void loadJMeterProperties(File resourceDir) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/bin/jmeter.properties");
    write(is, new FileOutputStream(new File(resourceDir, "jmeter.properties")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/bin/saveservice.properties");
    write(is, new FileOutputStream(new File(resourceDir, "saveservice.properties")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/bin/system.properties");
    write(is, new FileOutputStream(new File(resourceDir, "system.properties")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/bin/upgrade.properties");
    write(is, new FileOutputStream(new File(resourceDir, "upgrade.properties")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/bin/user.properties");
    write(is, new FileOutputStream(new File(resourceDir, "user.properties")));
  }
  
  private void loadJMeterUtilities(File sourceDir) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/ListenToTest.java.tmpl");
    write(is, new FileOutputStream(new File(sourceDir, "ListenToTest.java")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/MapBuilder.java.tmpl");
    write(is, new FileOutputStream(new File(sourceDir, "MapBuilder.java")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/SamplerBuilder.java.tmpl");
    write(is, new FileOutputStream(new File(sourceDir, "SamplerBuilder.java")));
    
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jmeter/java/Utils.java.tmpl");
    write(is, new FileOutputStream(new File(sourceDir, "Utils.java")));    
  }
  
  private void compress(String projectId, String from, String to) throws IOException {
    File fromDir = new File(from);
    ZipOutputStream outDir = new ZipOutputStream(new FileOutputStream(to));
    write(projectId, fromDir, outDir);
    outDir.close();
  }
  
  private void write(String projectId, File file, ZipOutputStream outDir) throws IOException {
    byte[] buffer = new byte[4096]; // Create a buffer for copying
    int bytes_read;
    
    for (String entry : file.list()) {
      File f = new File(file, entry);
      if (f.isDirectory()) {
        write(projectId, f, outDir);
        continue;
      }
      FileInputStream fis = new FileInputStream(f);
      
      String path = f.getPath().substring(f.getPath().indexOf(projectId));
      ZipEntry zip = new ZipEntry(path);
      outDir.putNextEntry(zip);
      while ((bytes_read = fis.read(buffer)) != -1) {
        outDir.write(buffer, 0, bytes_read);
      }
      fis.close();
    }
  }
  
  private void write(InputStream is, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024];
    for (int l = is.read(buffer); l != -1; l = is.read(buffer)) {
      out.write(buffer, 0, l);
    }
    is.close();
    out.close();
  }
  
  private String getAvailableName(String name, Set<String> pool) {
    if (!pool.contains(name)) {
      pool.add(name);
      return name;
    }
    name = name + pool.size();
    return getAvailableName(name, pool);
  }
}
