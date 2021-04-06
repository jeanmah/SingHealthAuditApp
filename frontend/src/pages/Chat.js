import React, { useEffect, useContext } from "react";
import { Link } from 'react-router-dom';
import { Typography, Button } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import { FormGroup } from "@material-ui/core";

function Chat() {

  const {
    allChatsOfUserState,
    getAllChatsOfUser
  } = useContext(Context);

  useEffect(() => {
    getAllChatsOfUser();
  }, []);

  const styles = useStyles();

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">Chat</Typography>
      <Button
        className={styles.buttons}
        align="center"
        variant="outlined"
        color="primary"
        fullWidth
      >
        Get All Chats of User
      </Button>
    </main>
  )
}

export default Chat;