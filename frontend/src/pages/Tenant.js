import React from "react";
import { Link, useParams } from "react-router-dom";

function Tenant() {
  const { tenantid } = useParams();

  return <div>Tenant Page</div>;
}

export default Tenant;
