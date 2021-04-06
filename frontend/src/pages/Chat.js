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

  // allChatsOfUserState.map(chat => {
  //   console.log("chat: " + chat);
  //   console.log("chat ID: " + chat.chat_id);
  //   console.log("auditor ID: " + chat.auditor_id);
  //   console.log("tenant ID: " + chat.tenant_id);
  //   console.log("messages: " + chat.messages);
  // })

  const ChatInfo = (props) => {
    return (
      <div>
        <div>Chat ID: {props.chat_id}</div>
        <div>Tenant ID: {props.tenant_id}</div>
        <div>Auditor ID: {props.auditor_id}</div>
        <div>Messages: {props.messages}</div>
      </div>   
    );
  };

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
        onClick={() => {
          console.log("chatState: " + allChatsOfUserState);
          console.log("firstChat: " + allChatsOfUserState[0]);
          console.log("firstChatID: " + allChatsOfUserState[0].chat_id);
        }}
      >
        Get All Chats of User
      </Button>

      <div>
        {allChatsOfUserState.map((chat, index) => {
          console.log("Chats in chat page: " + chat.chat_id);
          console.log("Chat info: " + chat);
          return (
            <ChatInfo key={index}
              chat_id={chat.chat_id}
              tenant_id={chat.tenant_id}
              auditor_id={chat.auditor_id}
              messages={chat.messages}
            />
          )
        })}
      </div>

      {/* <ChatInfo 
        chat_id={allChatsOfUserState[0].chat_id}
        tenant_id={allChatsOfUserState[0].tenant_id}
        auditor_id={allChatsOfUserState[0].auditor_id}
        messages={allChatsOfUserState[0].messages}
      /> */}

    </main>
  )
}

export default Chat;