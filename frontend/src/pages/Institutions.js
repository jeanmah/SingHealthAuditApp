import React from "react";
import { institutions } from "../data";
import { Link } from "react-router-dom";
import { FaAngleRight } from "react-icons/fa";
// import CGH from "../images/cgh.png";
// import KKH from "../images/kkh.png";
// import SGH from "../images/sgh.png";
// import SKH from "../images/skh.png";
// import NCCS from "../images/nccs.png";
// import NDCS from "../images/ndcs.jpeg";

function Institutions() {
  return (
    <div className="institutions-head">
      {institutions.map((institution, index) => {
        const { id, name, tenantNames, imageUrl } = institution;
        return (
          <Link key={index} to={`/institution/${id}`}>
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
  );
}

export default Institutions;
