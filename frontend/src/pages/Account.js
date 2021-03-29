import React, {useEffect} from "react";
import { NavLink } from "react-router-dom";
import axios from "axios";


import Navbar from "../Navbar";

const API_URL = "http://localhost:8080";


// getUserProfile(@AuthenticationPrincipal UserDetails auditorUser,  int user_id) 
// - Returns account information of the specified_user at specified access_level DONE

function Account() {
  // useEffect(() => {
  //   return axios.get(`${API_URL}/account/getUserProfile`, {})
  //   .then((response) => {
  //     console.log(response.data);
  //   })
  // },[])
  
  const name = "";
  const id = "";
  const user_type = "";
  return (
    <div>
      <Navbar />
      <h2>Account</h2>
      <div>Name: {name}</div>
      <div>User ID: {id}</div>
      <div>User Type: {user_type}</div>
    </div>
  );
}

export default Account;
