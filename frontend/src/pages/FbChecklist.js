import React, { useState, useContext, useEffect, useCallback } from "react";
import { Link, useParams } from "react-router-dom";
import Question from "../components/Question";
import { Context } from "../Context";
import Modal from "../components/Modal";
import Navbar from "../Navbar";

function FbCategory() {
  //get tenant id from url
  const { tenantId } = useParams();
  //Context: Fb Checklist
  const {
    fbChecklistState,
    setFbChecklistState,
    getFbChecklistQuestions,
    fbReportState,
    setFbReportState,
    submitFbReport,
  } = useContext(Context);

  const [isLoading, setIsLoading] = useState(true);

  //Context: to call update audits function and reset checked values after clicking submit
  // const { updateAudit, resetTenantFbChecklist } = useContext(Context);
  // //Context: tenants state
  // const { tenantsState } = useContext(Context);

  //call this function when component is mounted
  useEffect(() => {
    //function to retrieve questions
    getFbChecklistQuestions();
  }, []);
  //to set isLoading to false and display HTML DOM elements when fbChecklistState is complete
  useEffect(() => {
    if (fbChecklistState.length === 96) {
      setIsLoading(false);
    }
  }, [fbChecklistState]);
  //function called when submit button is clicked

  //retrieve fbchecklist questions from backend
  // getFbChecklistQuestions("FB")
  //   .then((response) => {
  //     setFbChecklistState(response.data);
  //   })
  //   .catch(() => {
  //     console.log("failed to retrieve fbchecklist");
  //   });
  //create new array of questions with status to allow checking of pass/fail
  // const fbChecklistReport = prepareFbChecklistReport(fbChecklistState);

  // console.log(fbChecklistReport);

  // const tenantObject = tenantsState.find(
  //   (tenant) => tenant.tenantid === tenantId
  // );
  //destructure tenantObject
  // const { tenantid, tenantName, status, institution } = tenantObject;

  return (
    <div>
      {isLoading && <div>Loading</div>}
      {!isLoading && (
        <>
          <Navbar />
          <div className="category-head">
            <h2>FB Checklist</h2>
            {fbChecklistState.map((question, index) => {
              const { fb_qn_id, requirement } = question;
              return (
                <Question
                  key={index}
                  fb_qn_id={fb_qn_id}
                  requirement={requirement}
                  tenantId={tenantId}
                />
              );
            })}
            <br />
            <Link to={`/tenant/${tenantId}`}>
              <button
                // onClick={() => {
                //   updateAudit(tenantId, "FB", tenantName, "unresolved");
                //   resetTenantFbChecklist(tenantId);
                // }}
                onClick={() => submitFbReport(tenantId, fbReportState, "FB")}
              >
                Submit
              </button>
            </Link>
          </div>
        </>
      )}
    </div>
  );
}

export default FbCategory;
