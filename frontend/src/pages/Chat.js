import { FormGroup } from "@material-ui/core";
import { Typography, Button, Grid, TextField } from "@material-ui/core";
import React, { useState, useEffect, useContext, useLocation } from "react";
import { useParams } from "react-router-dom";

import { Context } from '../Context';
import Navbar from "../Navbar";
import useStyles from "../styles";

function Chat() {
  const { chatId } = useParams(); // typeof chatId: String
  const [chatEntriesState, setChatEntriesState] = useState([]);
  const [subjectState, setSubjectState] = useState("");
  const [bodyState, setBodyState] = useState("");
  const { 
    allChatsOfUserState,
    getChatEntriesOfUser, 
    postChatEntry, 
    accountState, 
  } = useContext(Context);
  const styles = useStyles();
  const { acc_id } = accountState;
  let currentChat = {};

  useEffect(() => {
    async function getResponse() {
      try{
        await getChatEntriesOfUser(chatId).then((response) => {
          //console.log("Chat: allChatsOfUser: " + response.data);
          setChatEntriesState(response.data);
          //console.log("state: " + chatEntriesState);
          allChatsOfUserState.map((chat, index) => {
            //console.log(chat.chat_id);
            //console.log(typeof chat.chat_id);
            if (chat.chat_id === parseInt(chatId)) {
              currentChat = chat;
              console.log("yes!")
            }
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
  }, []);

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

  // Modify number format
  function addZero(number) {
    if (number < 10) return "0" + number;
    else return number;
  };

  // Generate current date in YYYY-MM-DD
  function getDate() {
    var today = new Date();
    var year = today.getFullYear();
    var month = addZero(today.getMonth()+1);
    var day = addZero(today.getDate());
    return year + "-" + month + "-" + day;
  };

  // Generate current time in HH-MM-SS
  function getTime() {
    var today = new Date();
    var hours = addZero(today.getHours());
    var minutes = addZero(today.getMinutes());
    var seconds = addZero(today.getSeconds());
    return hours + ":" + minutes + ":" + seconds;
  }

  // Handle the SEND MESSAGE button click
  function handleClick() {
    let parentChatId = chatId;
    let subject = subjectState;
    let messageBody = bodyState;
    let attachments = null; // What is JSON node?
    let date = getDate();
    let time = getTime();
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
  }

  return (
    <main className={styles.main}>
      <Navbar />
      <Typography variant="h3" align='center'>Chat</Typography>
      <Typography align='center'>Chat ID: {chatId}</Typography>
      <br />
      <FormGroup column="true">
        {chatEntriesState.map((entry, index) => {
          return (
            <React.Fragment key={index}>
              {/* <Grid item xs={12}>
                <Typography>Chat Entry ID: {entry.chat_entry_id}</Typography>
                <Typography>Subject:{entry.subject}</Typography>
                <Typography>Sender ID: {entry.sender_id}</Typography>
                <Typography>Time: {entry.time}</Typography>
                <Typography>Date: {entry.date}</Typography>
                <Typography>Message Body: {entry.messageBody}</Typography>
                <Typography>Attachments: {entry.attachments}</Typography>
              </Grid> */}
              <div item className={isMine(entry.sender_id) ? styles.rightBubble : styles.leftBubble}>
                <Grid item xs={12} sm container>
                  <Grid item xs container direction="column" spacing={2}>
                    <Grid item xs>
                      <Typography gutterBottom variant="subtitle1">
                        Chat Entry ID: {entry.chat_entry_id}
                      </Typography>
                      <Typography variant="body2" gutterBottom>
                        Subject: {entry.subject}
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        Message Body: {entry.messageBody}
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        Attachments: {entry.attachments}
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid item>
                    <Typography variant="subtitle1">{entry.date}, {entry.time}</Typography>
                  </Grid>
                </Grid>
              </div>
            </React.Fragment>
          )
        })}
      </FormGroup>

      <div className={styles.chat_edit}>
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
          className={styles.buttons}
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

export default Chat;