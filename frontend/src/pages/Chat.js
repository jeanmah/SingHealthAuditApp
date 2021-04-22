<<<<<<< HEAD
import React, { useEffect, useContext, useState } from "react";
import { Link } from "react-router-dom";
import { Typography, Button } from "@material-ui/core";
import axios from "axios";
=======
import { FormGroup } from "@material-ui/core";
import { Typography, Button, Grid, TextField, List, ListItem } from "@material-ui/core";
import React, { useState, useEffect, useContext, useLocation } from "react";
import { useParams } from "react-router-dom";
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09

import { Context } from '../Context';
import Navbar from "../Navbar";
import useStyles from "../styles";
<<<<<<< HEAD
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
=======
import { getDateString, getTimeString } from "../components/utils";

function Chat() {
  const { chatId } = useParams(); // typeof chatId: String
  const [chatEntriesState, setChatEntriesState] = useState([]);
  const [subjectState, setSubjectState] = useState("");
  const [bodyState, setBodyState] = useState("");
  // Use a state to force 
  const { 
    allChatsOfUserState,
    getChatEntriesOfUser, 
    postChatEntry, 
    accountState, 
    chatSubmitState,
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09
  } = useContext(Context);
  const styles = useStyles();
  const { acc_id } = accountState;

  const [allChatsState, setAllChatsState] = useState([]);
  const styles = useStyles();
  const chatsArray = [];
  const chatEntriesArray = [];
  const chatsEntriesDict = {};

  const parentChatId = "2";
  const numLastestChatEntries = "1";

  useEffect(() => {
<<<<<<< HEAD
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
=======
    async function getResponse() {
      try{
        await getChatEntriesOfUser(chatId).then((response) => {
          console.log("Chat: allChatsOfUser: " + response.data);
          setChatEntriesState(response.data.reverse());
          //console.log("state: " + chatEntriesState);
          allChatsOfUserState.map((chat, index) => {
            //console.log(chat.chat_id);
            //console.log(typeof chat.chat_id);
            // if (chat.chat_id === parseInt(chatId)) {
            //   currentChat = chat;
            //   console.log("yes!")
            // }
          });
          //console.log(currentChat.chat_id);
          //console.log(chatId);
          //console.log(typeof chatId);
        })
      } catch {
        console.log("Failed to retrive allChatsOfUser");
      }
    };
    getResponse();
  }, [chatSubmitState]);

  //console.log(currentChat.chat_id);

  // const ChatHeader = () => {
  //   let friend_id = 0;
  //   if (auditor_id === acc_id) {
  //     friend_id = tenant_id;
  //   } else if (tenant_id === acc_id) {
  //     friend_id = auditor_id;
  //   } else {
  //     friend_id = null;
  //   }
  //   return (
  //     <div>Chat with {friend_id}</div>
  //   )
  // }

  // Check if the message is sent by the user, or received by the user
  function isMine(sender_id) {
    return (sender_id === acc_id);
  };

  // Handle the SEND MESSAGE button click
  function handleClick() {
    let parentChatId = chatId;
    let subject = subjectState;
    let messageBody = bodyState;
    let attachments = null; // What is JSON node?
    let date = getDateString(new Date());
    let time = getTimeString();
    console.log("This is calling postNewChatEntry");
    console.log("Date: " + date);
    console.log("Time: " + time);
    console.log("Chat ID: " + parentChatId);
    console.log("Subject: " + subject);
    console.log("Message Body: " + messageBody);
    postChatEntry(parentChatId, subject, messageBody, attachments);
  };

  function subjectChangeHandler(subject_input) {
    setSubjectState(subject_input);
  };

  function bodyChangeHandler(body_input) {
    setBodyState(body_input);
  };
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09

  return (
    <main className={styles.main}>
      <Navbar />
      <br />
<<<<<<< HEAD
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
=======
      <Typography variant="h5" align='center'>Chat ID: {chatId}</Typography>
      <br />
      <ul className={styles.chat_entries_list}>
        {chatEntriesState.map((entry, index) => {
          return (
            <React.Fragment key={index}>
              <li item className={isMine(entry.sender_id) ? styles.rightBubble : styles.leftBubble}>
                <Grid item xs={12} sm container>
                  <Grid item xs container direction="column" spacing={2}>
                    <Grid item xs>
                      <Typography variant="subtitle2" color="textSecondary">
                        Subject: {entry.subject}
                      </Typography>
                      <Typography variant="body1">
                        {entry.messageBody}
                      </Typography>
                      
                    </Grid>
                  </Grid>
                  <Grid item>
                    <Typography variant="body2" color="textSecondary">
                      {entry.date}, {entry.time}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      {entry.attachments}
                    </Typography>
                  </Grid>
                </Grid>
              </li>
            </React.Fragment>
          )
        })}
      </ul>
      <div className={styles.chat_entry_edit}>
        <FormGroup column="true">
          <TextField 
            className={styles.message_input} 
            label="Subject" 
            variant="outlined" 
            onChange={(e) => subjectChangeHandler(e.target.value)}
          />
          <TextField 
            className={styles.message_input} 
            label="Message" 
            variant="outlined" 
            onChange={(e) => bodyChangeHandler(e.target.value)}
          />
        </FormGroup>
        
        <Button
          className={styles.big_buttons}
          align="center"
          variant="outlined"
          fullWidth
          color="primary"
          onClick={() => handleClick()}
        >
          Send Message
        </Button>
      </div>
    </main>
  )
};
>>>>>>> a8b9d981ebbb5f4adcb4ece47154562ce623ac09

export default Chat;
