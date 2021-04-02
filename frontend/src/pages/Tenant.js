import React, { useContext, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Context } from "../Context";
import Navbar from "../Navbar";

function Tenant() {
  //get tenantid from url
  const { tenantId } = useParams();
  //tenant state
  const [tenantState, setTenantState] = useState();
  //Context: getUserInfo method
  const { getUserInfo } = useContext(Context);

  useEffect(() => {
    getUserInfo(tenantId)
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });
  }, []);

  // const { tenantsState} = useContext(Context);
  // //Context: audits state
  // const { auditsState, updateAudit } = useContext(Context);

  //get tenant objects among tenants array
  // const tenantObject = tenantsState.find(
  //   (tenant) => tenant.tenantid === tenantId
  // );

  // const {
  //   tenantid,
  //   tenantName,
  //   timeRemaining,
  //   status,
  //   institution,
  //   latestScore,
  // } = tenantObject;

  // const tenantAudits = auditsState.filter((audit) => {
  //   return audit.tenantid === tenantId;
  // });
  return (
    <div>tenant</div>
    // <div className="tenant-page">
    //   <Navbar />
    //   <div className="tenant-name">{tenantName}</div>
    //   <div className="tenant-container">
    //     {/* <section className="tenant-content">
    //       {tenantAudits.map((tenant, index) => {
    //         return <div key={index}>{tenant.score}</div>;
    //       })}
    //     </section> */}

    //     <div>View Chat</div>
    //     <div>View Previous Audits</div>
    //     <Link to={`/tenant/fbChecklist/${tenantid}`}>Conduct Audit</Link>
    //   </div>
    // </div>
  );
}

export default Tenant;
