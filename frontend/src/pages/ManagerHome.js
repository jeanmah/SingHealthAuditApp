import React, { useState } from "react";
import { audits } from "../data";
import { Link } from "react-router-dom";
import HomeCategories from "../components/HomeCategories";
import Audits from "../components/Audits";
import Navbar from "../Navbar";

function ManagerHome() {
  return (
    <main>
      <Navbar />
      <div>This is manager homepage</div>
    </main>
  );
}

export default ManagerHome;
