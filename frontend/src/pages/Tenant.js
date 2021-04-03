import React, { useContext, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Context } from "../Context";
import Navbar from "../Navbar";
import Loading from "./Loading";

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
        setTenantState(response.data);
      })
      .catch(() => {
        console.log("Failed to retrieve tenant info");
      });
  }, []);

  return (
    <div>
      {tenantState ? (
        <div className="tenant-page">
          <Navbar />
          <div className="tenant-name">{tenantState.store_name}</div>
          <div className="tenant-container">
            <div>View Chat</div>
            <div>View Previous Audits</div>
            <Link to={`/tenant/fbChecklist/${tenantId}`}>Conduct Audit</Link>
          </div>
        </div>
      ) : (
        <Loading />
      )}
    </div>
  );
}

export default Tenant;
