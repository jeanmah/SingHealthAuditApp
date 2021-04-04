import React, { useState, createContext, useCallback } from "react";
import { audits, fbChecklist, tenants, institutions } from "./data";
import axios from "axios";
import { Redirect } from "react-router-dom";
import AuthenticationService from "./AuthenticationService";

export const Context = createContext();

export const ContextProvider = (props) => {
  const API_URL = "http://localhost:8080";

  /*
  =============== 
  BACKEND
  ===============
  */

  /*
  ---------------
  FbChecklist
  ---------------
  */
  //function to get Fb Checklist questions
  const getFbChecklistQuestions = () => {
    AuthenticationService.getStoredAxiosInterceptor();

    return axios.get(`${API_URL}/report/getAllQuestions`, {
      params: { type: "FB" },
    });
  };

  //function to submit FbChecklist report to compute the score
  const submitFbReport = useCallback((tenantid, fbreport) => {
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("checklist", JSON.stringify(fbreport));
    return axios
      .post(
        `${API_URL}/report/postReportSubmission?type=FB&tenant_id=${tenantid}&remarks=`,
        formdata,
        {
          headers: {
            "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
          },
          // params: { type: "FB", tenant_id: t_id, remarks: "" },
          // data: formdata,
        }
      )
      .then((response) => {
        console.log(response);
        if (response.status === 200) {
          return <Redirect to={`/tenant/${tenantid}`} />;
        }
      })
      .catch(() => {
        console.log("Failed FB report submission");
      });
  });

  /*
  ---------------
  Institution
  ---------------
  */

  //function to get tenants in a particular institution
  const getInstitutionTenants = (name) => {
    AuthenticationService.getStoredAxiosInterceptor();

    return axios.get(`${API_URL}/account/getAllTenantsOfBranch`, {
      params: { branch_id: name },
    });
  };
  /*
  --------------- 
  Tenant
  ---------------
  */
  //function to get user info given user id
  const getUserInfo = (userId) => {
    AuthenticationService.getStoredAxiosInterceptor();

    return axios.get(`${API_URL}/account/getUserProfile`, {
      params: { user_id: parseInt(userId) },
    });
  };

  /*
  --------------- 
  Home Auditor
  ---------------
  */

  //function to get all the audits done given auditor's username
  const getAudits = (userName) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReportIDs`, {
      params: { username: userName, type: "ALL" },
    });
  };

  const getReport = (reportId) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReport`, {
      params: { report_id: parseInt(reportId) },
    });
  };

  /*
  =============== 
  FRONTEND
  ===============
  */
  //FRONTEND STATES AND FUNCTIONS
  //state for report
  const [fbReportState, setFbReportState] = useState([]);
  //state to keep track of audit
  const [auditsState, setAuditsState] = useState();
  //state to keep track of all tenants
  const [tenantsState, setTenantsState] = useState();
  //state for fbChecklist

  //state for institutions
  const [institutionsState, setInstitutionstate] = useState(institutions);
  //state of comments in modal
  const [comment, setComment] = useState("");

  //function to prepare report state
  const createFbReportState = useCallback((checklist, response) => {
    console.log(checklist);
    //create temporary array
    let array = [];
    checklist.forEach((question) => {
      const { fb_qn_id } = question;
      array.push({
        qn_id: fb_qn_id,
        status: false,
        severity: 0,
        remarks: "",
        images: "",
      });
    });
    //set fbreportstate to array
    setFbReportState(array);

    console.log("created fb report");
    console.log(array);
  }, []);

  //function to update audits state
  const updateAudit = (
    tenantid,
    type,
    tenantname,
    // timeremaining,
    status
    // date
  ) => {
    const tenantObject = tenantsState.find(
      (tenant) => tenant.tenantid === tenantid
    );
    const tenantFbChecklist = tenantObject.fbChecklist;
    let score = tenantFbChecklist.reduce((total, question) => {
      if (question.checked === true) {
        total += 1;
      }
      return total;
    }, 0);

    setAuditsState((prevAudits) => {
      return [
        ...prevAudits,
        {
          tenantid: tenantid,
          type: type,
          tenantname: tenantname,
          // timeremaining: timeremaining,
          status: status,
          // date: date,
          institution: tenantObject.institution,
          score: score,
        },
      ];
    });
  };

  //function to reset tenant's fb checklist checked values
  const resetTenantFbChecklist = (tenantId) => {
    const tenantObject = tenantsState.find(
      (tenant) => tenant.tenantid === tenantId
    );
    //fbchecklist property for a tenant
    const tenantFbChecklist = tenantObject.fbChecklist;
    //reset each checked to false
    tenantFbChecklist.forEach((question) => {
      question.checked = false;
    });
    // //get tenants array without tenantObject
    // const remainingTenants = tenantsState.filter((tenant) => {
    //   return tenant.tenantid !== tenantId;
    // });
    // //update state with newTenantObject
    // setTenantsState([...remainingTenants, tenantObject]);
  };

  //functions to update tenant comment property
  const updateTenantComment = (tenantId, questionId) => {
    //find object with specific tenantId
    const tenantObject = tenantsState.find((tenant) => {
      return tenant.tenantid === tenantId;
    });
    //fbchecklist property for a tenant
    const tenantFbChecklist = tenantObject.fbChecklist;
    //find question within the array of questions
    const checklistQuestion = tenantFbChecklist.find((question) => {
      return question.id === questionId;
    });
    //update checked property
    checklistQuestion.comment = comment;
    console.log(tenantId, questionId);
    console.log(tenantObject);
    console.log(tenantsState);
  };

  return (
    <Context.Provider
      value={{
        // openQuestionModal,
        // closeQuestionModal,
        tenantsState,
        setTenantsState,
        auditsState,
        setAuditsState,
        updateAudit,
        resetTenantFbChecklist,
        comment,
        setComment,
        updateTenantComment,
        getFbChecklistQuestions,

        fbReportState,
        setFbReportState,
        createFbReportState,
        submitFbReport,
        getInstitutionTenants,
        getUserInfo,
        getAudits,
        getReport,
      }}
    >
      {props.children}
    </Context.Provider>
  );
};