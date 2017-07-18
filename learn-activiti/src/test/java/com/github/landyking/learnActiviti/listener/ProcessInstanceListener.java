package com.github.landyking.learnActiviti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.Arrays;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/7 9:30
 * note:
 */
public class ProcessInstanceListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        if ("start".equals(execution.getEventName())) {
            System.out.println("***************** start ********************");
            execution.setVariable("signList", Arrays.asList("Landy", "John", "Marry", "Kubee"));
            execution.setVariable("officeCandidateUsers", "officeOne,officeTwo");
        } else if ("end".equals(execution.getEventName())) {
            System.out.println("************ end ****************");
            System.out.println("\t\tprocess key :"+execution.getProcessDefinitionId());
            System.out.println("\t\tbusiness key :"+execution.getProcessBusinessKey());
            System.out.println("\t\trevoked :"+execution.getVariable("revoked"));
        }
    }
}
