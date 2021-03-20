import React, { useState, useRef, useEffect } from "react";
import { FaBars } from "react-icons/fa";
import { navLinks, navBarImage } from "./data";
import auditor from "./auditor.png";
import { Link } from "react-router-dom";
function Navbar() {
  const [toggleClicked, settoggleClicked] = useState(false);
  const linksContainerRef = useRef(null);
  const linksRef = useRef(null);
  const showLinks = () => {
    settoggleClicked(!toggleClicked);
  };

  useEffect(() => {
    let linksHeight = linksRef.current.getBoundingClientRect().height;
    if (toggleClicked) {
      linksContainerRef.current.style.height = `${linksHeight}px`;
    } else {
      linksContainerRef.current.style.height = "0px";
    }
  }, [toggleClicked]);

  return (
    <nav>
      <div className="nav-pc">
        <div className="nav-mobile">
          <img src={auditor} className="logo" alt="auditor"></img>
          <div className="auditor-name">Welcome Marcus {}</div>
          <button
            className="nav-toggle"
            onClick={() => {
              showLinks();
            }}
          >
            <FaBars />
          </button>
        </div>
        <div className="links-container" ref={linksContainerRef}>
          <ul className="links" ref={linksRef}>
            {navLinks.map((link) => {
              const { id, url, text } = link;
              return (
                <li>
                  <Link to={url}>{text}</Link>
                </li>
              );
            })}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
