package com.github.landyking.learnActiviti;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/5 15:04
 * note:
 */
public class LeaveBillDiagram {

    //    private static String resource = "leaveBill.bpmn20.xml";;
    private static String resource = "revokeLeaveBill.bpmn20.xml";

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJdbcDriver("org.h2.Driver")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setActivityFontName("微软雅黑")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment().addClasspathResource(resource).deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        new BpmnAutoLayout(bpmnModel).execute();
        deploy = repositoryService.createDeployment().addBpmnModel(resource, bpmnModel).deploy();
        processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        File destination = new File("target/diagram.png");

        System.out.println(destination.getAbsolutePath());
        FileUtils.copyInputStreamToFile(repositoryService.getProcessDiagram(processDefinition.getId()), destination);
        File destinationBpmn = new File("target/" + resource);
        System.out.println(destinationBpmn.getAbsolutePath());
        FileUtils.copyInputStreamToFile(repositoryService.getResourceAsStream(deploy.getId(), resource), destinationBpmn);
    }
}
