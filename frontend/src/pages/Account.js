import React from "react";
import {NavLink} from "react-router-dom";

function Account({filterChecklistTypes}) {
  return (
    <div>
      <h2>Auditor Account</h2>
      <NavLink exact to="/fbCategoryA"><button>FB Checklist</button></NavLink>
    </div>
  )
}

export default Account;
