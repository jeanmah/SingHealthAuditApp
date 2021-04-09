import React, { useEffect, useContext, useState } from "react";
import { Link } from "react-router-dom";
import { Typography, Button } from "@material-ui/core";
import axios from "axios";

import { Context } from "../Context";
import Navbar from "../Navbar";
import useStyles from "../styles";
import { FormGroup } from "@material-ui/core";
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
      try {
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
            }
            chatsArray.push(newChat);
            console.log(newChat);
          });
          console.log(chatsArray);
          setAllChatsOfUserState(chatsArray);
        });
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }

      try {
        await getChatEntriesOfUser(parentChatId).then((response) => {
          console.log("chatEntriesOfUser: " + response.data);
          console.log(response.data[0]);
          console.log(response.data[1]);
          response.data.map((entry) => {
            console.log(entry);
            chatEntriesArray.push(entry);
          });
          console.log("chatEntriedArray: " + chatEntriesArray);
          setChatEntriesOfUserState(chatEntriesArray);
          console.log("chatEntriesState: " + chatEntriesOfUserState);
        });
      } catch {
        console.log("Failed to retrive chatEntriesOfUser");
      }
    }
    getResponse();
  }, []);

  console.log(allChatsOfUserState);
  const tenant_id = "1005";
  const auditor_id = "1003";

  function handleClick() {
    postCreateNewChat(auditor_id, tenant_id);
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
      <Typography variant="h3" align="center">
        Chat
      </Typography>
      <Button
        align="center"
        variant="outlined"
        color="primary"
        className={styles.buttons}
        fullWidth
      >
        New Chat
      </Button>
      <br />
      <br />
      <Typography align="center">All Chats of the User</Typography>
      <ChatCards />
      <br />
      <br />
    </main>
  );
}

export default Chat;
