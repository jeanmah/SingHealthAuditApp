import React, { useState, createContext } from "react";
import { audits, fbChecklist, tenants, institutions } from "./data";

export const Context = createContext();

export const ContextProvider = (props) => {
  //state to keep track of audit
  const [auditsState, setAuditsState] = useState(audits);
  //state to keep track of all tenants
  const [tenantsState, setTenantsState] = useState(tenants);
  //state for fbChecklist
  const [fbChecklistState, setFbChecklistState] = useState(fbChecklist);
  //state for institutions
  const [institutionsState, setInstitutionstate] = useState(institutions);
  //state of comments in modal
  const [comment, setComment] = useState("");
  //state of modal questionid

  //function to update tenantsState
  const updateFbChecklistChecked = (tenantId, questionId) => {
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
    checklistQuestion.checked = !checklistQuestion.checked;
    console.log(tenantsState);

    // const remainingQuestions = tenantFbChecklist.filter((question) => {
    //   return question.id !== questionId;
    // });

    // const newTenantChecklist = [...remainingQuestions, checklistQuestion];
    // //update tenantObject
    // tenantObject[fbChecklist] = newTenantChecklist;
    // //get tenants array without tenantObject
    // const remainingTenants = tenantsState.filter((tenant) => {
    //   return tenant.tenantid !== tenantId;
    // });

    // //update state with newTenantObject
    // setTenantsState([...remainingTenants, tenantObject]);
  };

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

  //functions to close and open modal
  const openQuestionModal = (questionId) => {
    console.log("clicked openmodal");
    const checklistQuestion = fbChecklistState.find((question) => {
      return question.id === questionId;
    });
    checklistQuestion.modalOpen = true;
    // const remainingQuestions = fbChecklistState.filter((question) => {
    //   return question.id !== questionId;
    // });
    // setFbChecklistState([...remainingQuestions, checklistQuestion]);
  };
  const closeQuestionModal = (questionId) => {
    console.log("clicked closemodal");
    const checklistQuestion = fbChecklistState.find((question) => {
      return question.id === questionId;
    });
    checklistQuestion.modalOpen = false;
    // const remainingQuestions = fbChecklistState.filter((question) => {
    //   return question.id !== questionId;
    // });
    // setFbChecklistState([...remainingQuestions, checklistQuestion]);
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

    // const remainingQuestions = tenantFbChecklist.filter((question) => {
    //   return question.id !== questionId;
    // });
    // const newTenantChecklist = [...remainingQuestions, checklistQuestion];
    // //update tenantObject
    // tenantObject[fbChecklist] = newTenantChecklist;
    // //get tenants array without tenantObject
    // const remainingTenants = tenantsState.filter((tenant) => {
    //   return tenant.tenantid !== tenantId;
    // });

    // //update state with newTenantObject
    // setTenantsState([...remainingTenants, tenantObject]);
    // console.log(tenantsState);
  };

  return (
    <Context.Provider
      value={{
        openQuestionModal,
        closeQuestionModal,
        tenantsState,
        setTenantsState,
        updateFbChecklistChecked,
        fbChecklistState,
        setFbChecklistState,
        auditsState,
        setAuditsState,
        updateAudit,
        resetTenantFbChecklist,
        comment,
        setComment,
        updateTenantComment,
      }}
    >
      {props.children}
    </Context.Provider>
  );
};
