import React, { useState } from "react";
import { audits } from "../data";
import { Link } from "react-router-dom";
import HomeCategories from "../components/HomeCategories";
import Audits from "../components/Audits";
import Navbar from "../Navbar";

function AuditorHome() {
  //home audits displayed
  const [homeAudits, setHomeAudits] = useState(audits);

  //filter audits based on category
  const filterAudits = (category) => {
    if (category === "all") {
      setHomeAudits(audits);
    } else {
      const filteredAudits = audits.filter(
        (audit) => audit.status === category
      );
      setHomeAudits(filteredAudits);
    }
  };

  return (
    <main>
      <Navbar />
      <div>
        <HomeCategories filterAudits={filterAudits} />
        <Audits homeAudits={homeAudits} />
      </div>
    </main>
  );
}

export default AuditorHome;
