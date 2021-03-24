import React, { useContext, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { Context } from "../Context";

function Tenant() {
  //get tenantid from url
  const { tenantId } = useParams();

  //Context: tenants state
  const { tenantsState } = useContext(Context);
  //Context: audits state
  const { auditsState, updateAudit } = useContext(Context);

  //get tenant objects among tenants array
  const tenantObject = tenantsState.find(
    (tenant) => tenant.tenantid === tenantId
  );

  const {
    tenantid,
    tenantName,
    timeRemaining,
    status,
    institution,
    latestScore,
  } = tenantObject;

  console.log(auditsState);

  const tenantAudits = auditsState.filter((audit) => {
    return audit.tenantid === tenantId;
  });
  return (
    <div className="tenant-container">
      <section className="tenant-header">
        <div>hello {tenantName}</div>
      </section>
      <section className="tenant-content">
        {tenantAudits.map((tenant, index) => {
          return <div key={index}>{tenant.score}</div>;
        })}
      </section>
      <Link to={`/tenant/fbChecklist/${tenantid}`}>Conduct Audit</Link>
    </div>
  );
}

export default Tenant;
