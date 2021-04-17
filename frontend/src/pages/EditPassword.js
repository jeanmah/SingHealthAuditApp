import React, { useState, useContext } from "react";
import { Redirect } from "react-router-dom";
import { Typography, Button, TextField, FormGroup } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import useStyles from "../styles";
import Navbar from "../Navbar";

function EditPassword() {
  const {
    accountState,
    getAccountInfo
  } = useContext(Context);

  const API_URL = "http://localhost:8080";
  const [passwordState, setPasswordState] = useState("");
  const [confirmPasswordState, setConfirmPasswordState] = useState("");
  const styles = useStyles();

  function submitNewPassword(newPassword, confirmedNewPassword) {
    if (newPassword != confirmedNewPassword) {
      console.log("Two passwords are not the same.")
    } else {
      postPasswordChange(newPassword);
      console.log("Password change updated.")
    }
  }

  function clickRedirect() {
    console.log("Redirecting...");
    return <Redirect to="/" />;
  }

  function postPasswordChange(newPassword) {
    console.log("This is posting password change");
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("new_password", newPassword);
    return axios
      .post(
        `${API_URL}/account/postPasswordUpdate`,
        formdata,
        {
          headers: {
            "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
          },
        }
      )
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed password change submission");
      })
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">Edit Password</Typography>
      <FormGroup column="true">
        <TextField label="New Password" onChange={(e) => setPasswordState(e.target.value)}/>
        <TextField label="Confirm New Password" onChange={(e) => setConfirmPasswordState(e.target.value)}/>
      </FormGroup>
      <Button 
        align="center"
        variant="outlined"
        color="primary"
        className={styles.buttons}
        fullWidth
        onClick={() => submitNewPassword(passwordState, confirmPasswordState)}
      >
        Submit
      </Button>
      <Button 
        align="center"
        variant="outlined"
        color="primary"
        className={styles.buttons}
        fullWidth
        onClick={() => clickRedirect()}
      >
        Redirect
      </Button>
    </main>
  )
}

export default EditPassword;