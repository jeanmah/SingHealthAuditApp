import React, { useRef } from "react";
import { FaAngleRight } from "react-icons/fa";
import { Link } from "react-router-dom";

function Audits({ homeAudits }) {
  //to display the audits on the home page
  //change color based on whether it is resolved or not
  const homeAuditGridRef = useRef(null);

  return (
    <div>
      {homeAudits.map((audit, index) => {
        const { tenantid, tenantName, timeRemaining, status, date } = audit;
        
        return (
          <>
            {/* {" "}
            {audit.status === "resolved" ? (
              <div className="homeaudit-grid-resolved" ref={homeAuditGridRef}>
                <div className="tenantName-gridItem">{tenantName}</div>
                <Link className="btn-homeToTenant" to={`/tenant/${tenantid}`}>
                  <FaAngleRight />
                </Link>
              </div>
            ) : (
              <div className="homeaudit-grid-unresolved" ref={homeAuditGridRef}>
                <div className="tenantName-gridItem">{tenantName}</div>
                <Link className="btn-homeToTenant" to={`/tenant/${tenantid}`}>
                  <FaAngleRight />
                </Link>
              </div>
            )} */}{" "}
            {audit.status === "resolved" ? (
              <Link to={`/tenant/${tenantid}`}>
                <div className="homeaudit-grid-resolved" ref={homeAuditGridRef}>
                  <div className="tenantName-gridItem">{tenantName}</div>
                  <div className="btn-homeToTenant">
                    <FaAngleRight />
                  </div>
                </div>
              </Link>
            ) : (
              <Link to={`/tenant/${tenantid}`}>
                <div className="homeaudit-grid-unresolved" ref={homeAuditGridRef}>
                  <div className="tenantName-gridItem">{tenantName}</div>
                  <div className="btn-homeToTenant">
                    <FaAngleRight />
                  </div>
                </div>
              </Link>
            )}
          </>
        );
      })}
    </div>
  );
}

export default Audits;
