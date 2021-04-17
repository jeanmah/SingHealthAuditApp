import React, { useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { Context } from '../Context';
import { makeStyles } from '@material-ui/core/styles';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, Grid, FormGroup, Typography } from "@material-ui/core";

import ChatEntriesCards from "./ChatEntriesCard";
import useStyles from "../styles";


const ChatCards = (props) => {

  const styles = useStyles();
  const test = 147;

  return (
    <Grid item xs={12}>
      <NavLink to={{pathname: `/chat/${props.chat.chat_id}`, test: test}}>
        <Button
          className={styles.buttons}
          align="center"
          variant="outlined"
          fullWidth
          color="primary"
        >
          Chat ID: {props.chat.chat_id}
          Tenant ID: {props.chat.tenant_id}
        </Button>
      </NavLink>
    </Grid>
  )
}

export default ChatCards;