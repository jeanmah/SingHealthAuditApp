import React, { useState, createContext, useCallback } from "react";
import { audits, fbChecklist, tenants, institutions } from "./data";
import axios from "axios";
import { Redirect } from "react-router-dom";
import AuthenticationService from './AuthenticationService';

export const Context = createContext();

export const ContextProvider = (props) => {
  //BACKEND  FUNCTIONS
  const API_URL = "http://localhost:8080";

  const getAccountInfo = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    console.log("this is calling getAccountInfo");
    return axios
      .get(`${API_URL}/account/getUserProfile`, {
        params: {},
      })
      .then((response) => {
        console.log("Response from getUserProfile", response.data);
        setAccountState(response.data);
      })
      .catch(() => {
        console.log("userProfile retrieval failed");
      });
  }

  const getAllChatsOfUser = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    console.log("This is calling getAllChatsOfUser");
    return axios
      .get(`${API_URL}/chat/getAllChatsOfUser`, {
        params: {},
      })
      .then((response) => {
        console.log("Response from getAllChatsOfUser", response.data);
        setAllChatsOfUserState(response.data);
      })
      // .then((response) => {
      //   console.log("Response from getAllChatsOfUser", response.data);
      //   // Since the response.data is an array of chats, use map() to push chats into the state one by one
      //   response.data.map(chat => {
      //     setAllChatsOfUserState([]); // Before pushing, clear the original state
      //     console.log("Pushing new chat: " + chat.chat_id);
      //     const chats = allChatsOfUserState;
      //     chats.push(chat);
      //     setAllChatsOfUserState(chats);
      //   })
      //   console.log("All chats pushed: " + allChatsOfUserState);
      //   console.log("Type of allChatsOfUserState: " + typeof allChatsOfUserState);
      //   console.log("Chat in chats: " + allChatsOfUserState[0]);
      //   console.log("Chat in chats: " + allChatsOfUserState[0].chat_id);
      // })
      .catch(() => {
        console.log("allChatsOfUser retrieval failed")
      })
  }

  const getFbChecklistQuestions = useCallback(() => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios
      .get(`${API_URL}/report/getAllQuestions`, {
        params: { type: "FB" },
      })
      .then((response) => {
        setFbChecklistState(response.data);
        createFbReportState(response.data);
      })
      .catch(() => {
        console.log("fb checklist retrieval failed");
      });
  }, []);

  const submitFbReport = useCallback((tenantid, fbreport) => {
    console.log(JSON.stringify(fbreport));
    console.log("reached here");
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

  //FRONTEND STATES AND FUNCTIONS
  //state for report
  const [fbReportState, setFbReportState] = useState([]);
  //state to keep track of audit
  const [auditsState, setAuditsState] = useState(audits);
  //state to keep track of all tenants
  const [tenantsState, setTenantsState] = useState(tenants);
  //state for fbChecklist
  const [fbChecklistState, setFbChecklistState] = useState(fbChecklist);
  //state for institutions
  //const [institutionsState, setInstitutionstate] = useState(institutions);
  //state for account
  const [accountState, setAccountState] = useState([]);
  //state for chats of user
  const [allChatsOfUserState, setAllChatsOfUserState] = useState([]);
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

        fbChecklistState,
        setFbChecklistState,
        auditsState,
        setAuditsState,
        updateAudit,
        resetTenantFbChecklist,
        
        comment,
        setComment,
        updateTenantComment,
        getFbChecklistQuestions,

        accountState,
        setAccountState,
        getAccountInfo,

        allChatsOfUserState,
        setAllChatsOfUserState,
        getAllChatsOfUser,

        fbReportState,
        setFbReportState,
        createFbReportState,
        submitFbReport,
      }}
    >
      {props.children}
    </Context.Provider>
  );
};
