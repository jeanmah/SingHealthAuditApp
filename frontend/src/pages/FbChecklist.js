import React, { useState, useContext, useEffect, useCallback } from "react";
import { useParams } from "react-router-dom";
import Question from "../components/Question";
import { Context } from "../Context";
import Loading from "./Loading";
import Navbar from "../Navbar";

function FbCategory() {
  //get tenant id from url
  const { tenantId } = useParams();

  const [fbChecklistState, setFbChecklistState] = useState();
  //Context: Fb Checklist
  const {
    getFbChecklistQuestions,
    fbReportState,
    submitFbReport,
    createFbReportState,
  } = useContext(Context);

  useEffect(() => {
    //function to retrieve questions
    getFbChecklistQuestions()
      .then((response) => {
        setFbChecklistState(response.data);
        createFbReportState(response.data);
      })
      .catch(() => {
        console.log("fb checklist retrieval failed");
      });
  }, []);

  return (
    <div>
      {fbChecklistState ? (
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
            {/* <Link to={`/tenant/${tenantId}`}> */}
            <button
              // onClick={() => {
              //   updateAudit(tenantId, "FB", tenantName, "unresolved");
              //   resetTenantFbChecklist(tenantId);
              // }}
              onClick={() => submitFbReport(1006, fbReportState)}
            >
              Submit
            </button>
            {/* </Link> */}
          </div>
        </>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default FbCategory;
