import React from "react";
import { institutions } from "../data";
import { Link } from "react-router-dom";
import { FaAngleRight } from "react-icons/fa";
import Navbar from "../Navbar";

function Institutions() {
  return (
    <>
      <Navbar />
      <div className="institutions-head">
        {institutions.map((institution, index) => {
          const { id, name, tenantNames, imageUrl } = institution;
          return (
            <Link key={index} to={`/institution/${name}`}>
              <article key={id} className="institutions-institution">
                <img src={imageUrl} className="hospital-logo"></img>
                <header className="institutions-btn">
                  <span className="institutions-btnicon">
                    <FaAngleRight />
                  </span>
                </header>
              </article>
            </Link>
          );
        })}
      </div>
    </>
  );
}

export default Institutions;
