import React, { useEffect, useContext } from "react";
import HomeAuditorCards from "../components/HomeAuditorCards";
import HomeAuditorTabs from "../components/HomeAuditorTabs";
import Navbar from "../Navbar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { Context } from "../Context";

function HomeAuditor() {
  const { getAudits } = useContext(Context);

  useEffect(() => {
    const username = sessionStorage.getItem("authenticatedUser");
    console.log(username);
    getAudits(username)
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed to retrieve audits");
      });
  });

  return (
    <div>
      <CssBaseline />
      <Navbar />
      <HomeAuditorTabs />
      <HomeAuditorCards />
    </div>
  );
}

export default HomeAuditor;
