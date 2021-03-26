import React, { useState, useContext } from "react";
import { Link, useParams } from "react-router-dom";
import Question from "../components/Question";
import { Context } from "../Context";
import Modal from "../components/Modal";
import Navbar from "../Navbar";

function FbCategory() {
  //get tenant id from url
  const { tenantId } = useParams();
  //Context: Fb Checklist
  const { fbChecklistState } = useContext(Context);
  //Context: to call update audits function and reset checked values after clicking submit
  const { updateAudit, resetTenantFbChecklist } = useContext(Context);
  //Context: tenants state
  const { tenantsState } = useContext(Context);

  const tenantObject = tenantsState.find(
    (tenant) => tenant.tenantid === tenantId
  );
  //destructure tenantObject
  const { tenantid, tenantName, status, institution } = tenantObject;

  return (
    <>
      <Navbar />
      <div className="category-head">
        {fbChecklistState.map((question) => {
          const { id } = question;
          return <Modal key={id} questionId={id} tenantId={tenantId} />;
        })}

        <h2>FB Checklist</h2>
        {fbChecklistState.map((question, index) => {
          const { id, text } = question;
          return (
            <Question key={index} id={id} text={text} tenantId={tenantId} />
          );
        })}
        <br />

        <Link to={`/tenant/${tenantId}`}>
          <button
            onClick={() => {
              updateAudit(tenantId, "FB", tenantName, "unresolved");
              resetTenantFbChecklist(tenantId);
            }}
          >
            Submit
          </button>
        </Link>
      </div>
    </>
  );
}

export default FbCategory;
