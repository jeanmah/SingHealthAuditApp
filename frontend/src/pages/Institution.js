import React from "react";
import { tenants, institutions } from "../data";
import { Link, useParams } from "react-router-dom";
import { FaAngleRight } from "react-icons/fa";
import institutionImage from "../images/institutionhome.png";
import Navbar from "../Navbar";

function Institution() {
  //obtain id which is indicated in the url
  const { institutionid } = useParams();
  //array of one object which is the selected institution
  const selectedInstitution = institutions.filter((institution) => {
    const { id } = institution;
    return institutionid === id;
  });
  //filter the tenants based on the selected institution
  const selectedTenants = tenants.filter((tenant) => {
    const { institution } = tenant;
    return institutionid === institution;
  });

  return (
    <>
      <Navbar />
      {selectedInstitution.map((institution, index) => {
        const { id, name, imageUrl } = institution;
        return (
          <section key={id} className="institution-header">
            <img src={imageUrl} className="institution-logo" alt="logo"></img>
          </section>
        );
      })}
      {selectedTenants.map((tenant, index) => {
        const { tenantid, tenantName, status } = tenant;
        return (
          <>
            {tenant.status === "resolved" ? (
              <Link key={tenantid} to={`/tenant/${tenantid}`}>
                <section className="institution-tenant-resolved">
                  <div>{tenantName}</div>
                  <div className="institution-tenantbtn">
                    <FaAngleRight />
                  </div>
                </section>
              </Link>
            ) : (
              <Link key={tenantid} to={`/tenant/${tenantid}`}>
                <section className="institution-tenant-unresolved">
                  <div>{tenantName}</div>
                  <div className="institution-tenantbtn">
                    <FaAngleRight />
                  </div>
                </section>
              </Link>
            )}
          </>
        );
      })}
    </>
  );
}

export default Institution;
