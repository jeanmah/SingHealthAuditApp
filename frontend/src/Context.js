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

  const getAccountInfo = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    // console.log("this is calling getAccountInfo");
    return axios
      .get(`${API_URL}/account/getUserProfile`, {
        params: {},
      })
      .then((response) => {
        // console.log("Response from getUserProfile", response.data);
        setAccountState(response.data);
      })
      .catch(() => {
        console.log("userProfile retrieval failed");
      });
  };

  const getAllChatsOfUser = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    console.log("This is calling getAllChatsOfUser");
    return axios
      .get(`${API_URL}/chat/getAllChatsOfUser`, {
        params: {},
      });
  };

  const getChatEntriesOfUser = (chatId) => {
    AuthenticationService.getStoredAxiosInterceptor();
    console.log("This is calling getChatEntriesOfUser");
    //console.log(typeof parseInt(chatId));
    //console.log(typeof parseInt(numLastestEntries));
    return axios
      .get(`${API_URL}/chat/getChatEntriesOfUser`, {
        params: {
          parentChatId: parseInt(chatId),
          //numLastestChatEntries: parseInt(numLastestChatEntries),
        },
      });
  };

  // function to post a new chat with another user
  const postCreateNewChat = useCallback((auditor_id, tenant_id) => {
    AuthenticationService.getStoredAxiosInterceptor();
    console.log(auditor_id);
    console.log(tenant_id);
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append('auditor_id', auditor_id);
    formdata.append('tenant_id', tenant_id);
    return axios
      .post(
        `${API_URL}/chat/postCreateNewChat?auditor_id=${auditor_id}&tenant_id=${tenant_id}`, {
          params: {
            auditor_id: parseInt(auditor_id),
            tenant_id: parseInt(tenant_id),
          }
        }
        // formdata,
        // {
        //   // headers: {
        //   //   "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
        //   // },
        // }
      )
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed new chat creation");
      });
  });

  // function to post a new chat entry (message) in an existing chat
  const postChatEntry = useCallback((parentChatId, subject, messageBody, attachments) => {
    AuthenticationService.getStoredAxiosInterceptor();
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("parentChatId", parentChatId); // Integer
    formdata.append("subject", subject); // String
    formdata.append("messageBody", messageBody); // String
    formdata.append("attachments", attachments); // JsonNode with JsonArray of attachment strings, key should be "attachments"
    return axios
      .post(
        `${API_URL}/chat/postChatEntry`,
        // `${API_URL}/chat/postChatEntry?auditor_id=${auditor_id}&tenant_id=${tenant_id}`, {
        //   params: {
        //     parentChatId: parentChatId,
        //     subject: subject,
        //     messageBody: messageBody,
        //     attachments: attachments,
        //   }
        // }
        formdata,
        {
          headers: {
            "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
          },
        }
      )
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed new chat creation");
      });
  });

  //function to get Fb Checklist questions
  const getFbChecklistQuestions = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    //   return axios
    //     .get(`${API_URL}/report/getAllQuestions`, {
    //       params: { type: "FB" },
    //     })
    //     .then((response) => {
    //       setFbChecklistState(response.data);
    //       createFbReportState(response.data);
    //     })
    //     .catch(() => {
    //       console.log("fb checklist retrieval failed");
    //     });
    // }, []);

    return axios.get(`${API_URL}/report/getAllQuestions`, {
      params: { type: "FB" },
    });
  };

  //function to submit FbChecklist report to compute the score
  const submitFbReport = useCallback((tenantid, fbreport) => {
    console.log(fbreport);
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
        // if (response.status === 200) {
        //   return <Redirect to={`/tenant/${tenantid}`} />;
        // }
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
    console.log(name);
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

  const getUserInfoNoParams = () => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/account/getUserProfile`);
  };

  /*
  --------------------- 
  Home Auditor & Tenant
  ----------------------
  */

  //function to get all the audits done given auditor's username
  const getAudits = (userName) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReportIDs`, {
      params: { username: userName, type: "ALL" },
    });
  };

  const getTenantAudits = (tenantid) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReportIDs`, {
      params: { user_id: tenantid, type: "ALL" },
    });
  };

  // const getClosedTenantAudits = (tenantid) => {
  //   AuthenticationService.getStoredAxiosInterceptor();
  //   return axios.get(`${API_URL}/report/getReportIDs`, {
  //     params: { user_id: tenantid, type: "CLOSED" },
  //   });
  // };

  const getReport = (reportId) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReport`, {
      params: { report_id: parseInt(reportId) },
    });
  };

  const getReportStats = (reportId) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReportStatistics`, {
      params: { report_id: parseInt(reportId) },
    });
  };

  const getReportEntry = (reportId, entryId) => {
    AuthenticationService.getStoredAxiosInterceptor();
    return axios.get(`${API_URL}/report/getReportEntry`, {
      params: { report_id: parseInt(reportId), entry_id: entryId },
    });
  };

  // const filterAudits = (category) => {
  //   console.log(auditsState);
  // };

  /*
  =============== 
  FRONTEND
  ===============
  */
  //FRONTEND STATES AND FUNCTIONS
  //state for report ids
  const [reportIdsState, setReportIdsState] = useState();
  //state for report
  const [fbReportState, setFbReportState] = useState([]);
  //state to keep track of audit
  const [auditsState, setAuditsState] = useState();
  //state to keep track of all tenants
  const [tenantState, setTenantState] = useState();
  //state for fbChecklist

  //state for institutions
  //const [institutionsState, setInstitutionstate] = useState(institutions);
  //state for account
  const [accountState, setAccountState] = useState([]);
  //state for chats of user
  const [allChatsOfUserState, setAllChatsOfUserState] = useState([]);
  //state for chat entries of a chat
  const [chatEntriesOfUserState, setChatEntriesOfUserState] = useState([]);
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
        status: true,
        severity: 0,
        remarks: "",
        images: "",
      });
    });
    //set fbreportstate to array
    setFbReportState(array);
  }, []);

  //function to update audits state
  // const updateAudit = (
  //   tenantid,
  //   type,
  //   tenantname,
  //   // timeremaining,
  //   status
  //   // date
  // ) => {
  //   const tenantObject = tenantsState.find(
  //     (tenant) => tenant.tenantid === tenantid
  //   );
  //   const tenantFbChecklist = tenantObject.fbChecklist;
  //   let score = tenantFbChecklist.reduce((total, question) => {
  //     if (question.checked === true) {
  //       total += 1;
  //     }
  //     return total;
  //   }, 0);

  //   setAuditsState((prevAudits) => {
  //     return [
  //       ...prevAudits,
  //       {
  //         tenantid: tenantid,
  //         type: type,
  //         tenantname: tenantname,
  //         // timeremaining: timeremaining,
  //         status: status,
  //         // date: date,
  //         institution: tenantObject.institution,
  //         score: score,
  //       },
  //     ];
  //   });
  // };

  //function to reset tenant's fb checklist checked values
  // const resetTenantFbChecklist = (tenantId) => {
  //   const tenantObject = tenantsState.find(
  //     (tenant) => tenant.tenantid === tenantId
  //   );
  //   //fbchecklist property for a tenant
  //   const tenantFbChecklist = tenantObject.fbChecklist;
  //   //reset each checked to false
  //   tenantFbChecklist.forEach((question) => {
  //     question.checked = false;
  //   });
  //   // //get tenants array without tenantObject
  //   // const remainingTenants = tenantsState.filter((tenant) => {
  //   //   return tenant.tenantid !== tenantId;
  //   // });
  //   // //update state with newTenantObject
  //   // setTenantsState([...remainingTenants, tenantObject]);
  // };

  //functions to update tenant comment property
  // const updateTenantComment = (tenantId, questionId) => {
  //   //find object with specific tenantId
  //   const tenantObject = tenantsState.find((tenant) => {
  //     return tenant.tenantid === tenantId;
  //   });
  //   //fbchecklist property for a tenant
  //   const tenantFbChecklist = tenantObject.fbChecklist;
  //   //find question within the array of questions
  //   const checklistQuestion = tenantFbChecklist.find((question) => {
  //     return question.id === questionId;
  //   });
  //   //update checked property
  //   checklistQuestion.comment = comment;
  //   console.log(tenantId, questionId);
  //   console.log(tenantObject);
  //   console.log(tenantsState);
  // };

  return (
    <Context.Provider
      value={{
        // openQuestionModal,
        // closeQuestionModal,
        tenantState,
        setTenantState,
        auditsState,
        setAuditsState,
        // resetTenantFbChecklist,

        comment,
        setComment,
        // updateTenantComment,
        getFbChecklistQuestions,

        accountState,
        setAccountState,
        getAccountInfo,

        allChatsOfUserState,
        setAllChatsOfUserState,
        getAllChatsOfUser,
        postCreateNewChat,

        chatEntriesOfUserState,
        setChatEntriesOfUserState,
        getChatEntriesOfUser,
        postChatEntry,

        fbReportState,
        setFbReportState,
        createFbReportState,
        submitFbReport,
        getInstitutionTenants,
        getUserInfo,
        getUserInfoNoParams,
        getAudits,
        getReport,
        // getClosedTenantAudits,
        getTenantAudits,
        getReportStats,
        getReportEntry,
      }}
    >
      {props.children}
    </Context.Provider>
  );
};
