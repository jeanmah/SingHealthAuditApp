import React, { useContext, useEffect, useState } from "react";
import { Context } from '../Context';
import { Typography, Button, Grid, ButtonBase } from '@material-ui/core';

import Chat from "../pages/AllChats";
import useStyle from "../styles";

const ChatEntriesCards = () => {
  const { chatEntriesOfUserState, setChatEntriesOfUserState } = useContext(Context);
  const styles = useStyle();
  console.log("chatEntries: " + chatEntriesOfUserState);
  console.log("dict: " + chatEntriesOfUserState[2]);
  return (
    <Grid container>
      {chatEntriesOfUserState && (chatEntriesOfUserState.map((entry, index) => {
          return (
            <React.Fragment key={index}>
              <Grid item>
                <Typography>Chat Entry ID: {entry.chat_entry_id}</Typography>
                <Typography>Subject:{entry.subject}</Typography>
                <Typography>Sender ID: {entry.sender_id}</Typography>
                <Typography>Time: {entry.time}</Typography>
                <Typography>Date: {entry.date}</Typography>
                <Typography>Message Body: {entry.messageBody}</Typography>
                <Typography>Attachments: {entry.attachments}</Typography>
              </Grid>
              {/* <Grid item>
                <Grid item xs={12} sm container>
                  <Grid item xs container direction="column" spacing={2}>
                    <Grid item xs>
                      <Typography gutterBottom variant="subtitle1">
                        Standard license
                      </Typography>
                      <Typography variant="body2" gutterBottom>
                        Full resolution 1920x1080 • JPEG
                      </Typography>
                      <Typography variant="body2" color="textSecondary">
                        ID: 1030114
                      </Typography>
                    </Grid>
                    <Grid item>
                      <Typography variant="body2" style={{ cursor: 'pointer' }}>
                        Remove
                      </Typography>
                    </Grid>
                  </Grid>
                  <Grid item>
                    <Typography variant="subtitle1">$19.00</Typography>
                  </Grid>
                </Grid>
              </Grid> */}
            </React.Fragment>
          );}
        )
    )}
    </Grid>
  )
}

export default ChatEntriesCards;