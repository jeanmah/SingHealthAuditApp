import React, { useState, useEffect, useContext } from "react";
import { Typography, Button, TextField, FormGroup } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";

function EditAccount() {
  const { accountState, getAccountInfo } = useContext(Context);

  // All possible account info categories for 3 types of users
  const {
    acc_id,
    branch_id,
    employee_id,
    email,
    username,
    first_name,
    last_name,
    hp,
    mgr_id,
    role_id,
    store_id,
    type_id,
  } = accountState;

  const [usernameState, setUsernameState] = useState(username);
  const [firstnameState, setFirstnameState] = useState(first_name);
  const [lastnameState, setLastnameState] = useState(last_name);
  const [emailState, setEmailState] = useState(email);
  const [hpState, setHpState] = useState(hp);

  useEffect(() => {
    getAccountInfo();
  }, []);

  const API_URL = "http://localhost:8080";
  const styles = useStyles();
  const disabledInfo = [
    "Role",
    "Account ID",
    "Employee ID",
    "Branch ID",
    "Mgr ID",
    "Store ID",
    "Store Type",
  ];

  const EditAccountInfo = (props) => {
    if (props.info == null) {
      return null;
    } else if (disabledInfo.includes(props.category)) {
      return (
        <TextField
          label={props.category}
          defaultValue={props.info}
          disabled={true}
        />
      );
    } else {
      console.log("You shouldn't use this function.");
    }
  };

  function postAccountChange(
    newUsername,
    newFirstName,
    newLastName,
    newEmail,
    newHp
  ) {
    console.log("This is posting account changes");
    let payload = {
      username: newUsername,
      first_name: newFirstName,
      last_name: newLastName,
      email: newEmail,
      hp: newHp,
    };
    console.log(payload);
    let FormData = require("form-data");
    let formdata = new FormData();
    formdata.append("changes", JSON.stringify(payload));

    return axios
      .post(`${API_URL}/account/postProfileUpdate`, formdata, {
        headers: {
          "Content-Type": `multipart/form-data; boundary=${formdata._boundary}`,
        },
      })
      .then((response) => {
        console.log(response);
      })
      .catch(() => {
        console.log("Failed account change submission");
      });
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">
        Edit Account
      </Typography>
      <FormGroup column="true">
        <TextField
          label="Username"
          onChange={(e) => setUsernameState(e.target.value)}
        />
        <TextField
          label="First Name"
          onChange={(e) => setFirstnameState(e.target.value)}
        />
        <TextField
          label="Last Name"
          onChange={(e) => setLastnameState(e.target.value)}
        />
        <TextField
          label="Email"
          onChange={(e) => setEmailState(e.target.value)}
        />
        <TextField
          label="Contact Number"
          onChange={(e) => setHpState(e.target.value)}
        />
        <br />
        <EditAccountInfo category="Role" info={role_id} />
        <EditAccountInfo category="Account ID" info={acc_id} />
        <EditAccountInfo category="Branch ID" info={branch_id} />
        <EditAccountInfo category="Employee ID" info={employee_id} />
        <EditAccountInfo category="Mgr ID" info={mgr_id} />
        <EditAccountInfo category="Store ID" info={store_id} />
        <EditAccountInfo category="Store Type" info={type_id} />
        <br />
      </FormGroup>
      <Button
        className={styles.buttons}
        align="center"
        variant="outlined"
        color="primary"
        fullWidth
        onClick={() =>
          postAccountChange(
            usernameState,
            firstnameState,
            lastnameState,
            emailState,
            hpState
          )
        }
      >
        Submit
      </Button>
    </main>
  );
}

export default EditAccount;
