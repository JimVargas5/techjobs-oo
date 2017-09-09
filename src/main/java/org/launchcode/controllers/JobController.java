package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobFieldData;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job givenJob = JobData.getInstance().findById(id);
        model.addAttribute("givenJob", givenJob);
        model.addAttribute("title", givenJob.getName());
        /*ArrayList<Job> jobs = jobData.findAll();
        for (Job job : jobs){
            job.getId();
        }
        model.addAttribute("jobs", jobs);*/

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        Boolean validName = false;

        Boolean validEmployer = false;
        Employer newEmployer = null;

        Boolean validLocation = false;
        Location newLocation = null;

        Boolean validSkill = false;
        CoreCompetency newSkill = null;

        Boolean validType = false;
        PositionType newType = null;

        Job newJob = null;
        if (jobForm.getName().length() > 1){
            validName = true;
        }
        if (JobData.getInstance().getEmployers().findById(jobForm.getEmployerId()) != null){
            validEmployer = true;
            newEmployer = JobData.getInstance().getEmployers().findById(jobForm.getEmployerId());
        }
        if (JobData.getInstance().getLocations().findById(jobForm.getLocationId()) != null){
            validLocation = true;
            newLocation = JobData.getInstance().getLocations().findById(jobForm.getLocationId());
        }
        if (JobData.getInstance().getCoreCompetencies().findById(jobForm.getSkillId()) != null){
            validSkill = true;
            newSkill = JobData.getInstance().getCoreCompetencies().findById(jobForm.getSkillId());
        }
        if (JobData.getInstance().getPositionTypes().findById(jobForm.getTypeId()) != null){
            validType = true;
            newType = JobData.getInstance().getPositionTypes().findById(jobForm.getTypeId());
        }

        if (validName && validEmployer && validLocation && validSkill && validType){
            newJob = new Job(jobForm.getName(), newEmployer, newLocation, newType, newSkill);
            JobData.getInstance().add(newJob);
        }
        return "redirect:?id="+newJob.getId();

    }
}
