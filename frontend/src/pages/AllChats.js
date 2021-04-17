import React, { useEffect, useContext, useState } from "react";
import { Link } from 'react-router-dom';
import { Typography, Button, FormGroup, Grid } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import ChatCards from "../components/ChatCards";
import ChatEntriesCards from "../components/ChatEntriesCard";

function Chat() {

  const {
    setAllChatsOfUserState,
    allChatsOfUserState,
    getAllChatsOfUser,
    setChatEntriesOfUserState,
    chatEntriesOfUserState,
    getChatEntriesOfUser,
    postCreateNewChat,
  } = useContext(Context);

  const [allChatsState, setAllChatsState] = useState([]);
  const styles = useStyles();
  const chatsArray = [];
  const chatEntriesArray = [];
  const chatsEntriesDict = {};

  const parentChatId = "2";
  const numLastestChatEntries = "1";

  useEffect(() => {
    //getAllChatsOfUser();
    async function getResponse() {
      try{
        await getAllChatsOfUser().then((response) => {
          console.log("allChatsOfUser: " + response.data);
          response.data.map((chat) => {
            //console.log(chat);
            let newChat = {};
            newChat.chat_id = chat.chat_id;
            newChat.tenant_id = chat.tenant_id;
            newChat.auditor_id = chat.auditor_id;
            if (chat.messages.messages) {
              newChat.messages = [...chat.messages.messages];
            } else {
              newChat.messages = ["No message"];
            };
            chatsArray.push(newChat);
            //console.log(newChat);
          });
          //console.log(chatsArray);
          setAllChatsOfUserState(chatsArray);
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    }
    getResponse();
  }, []);

  //console.log(allChatsOfUserState);

  function handleClick() {
    let tenant_id = "1005";
    let auditor_id = "1003";
    console.log("Chat calling postNewChat");
    postCreateNewChat(auditor_id, tenant_id);
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">Chats</Typography>
      <Button 
        align="center"
        variant="outlined"
        color="primary"
        className={styles.buttons}
        fullWidth
        onClick={() => handleClick()}
      >
        New Chat
      </Button>
      <br />
      <br />
      <Typography align="center">All Chats of the User</Typography>
      <Grid container spacing={2}>
        {allChatsOfUserState.map((chat, index) => {
          return (
            <React.Fragment key={index}>
              <ChatCards chat={chat}/>
            </React.Fragment>
          );
        })}
      </Grid>
      
      <br />
      <br />
    </main>
  )
}

export default Chat;