import React, { useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { Context } from '../Context';
import { makeStyles } from '@material-ui/core/styles';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { Button, Grid, FormGroup, Typography } from "@material-ui/core";

import useStyles from "../styles";


const ChatCards = (props) => {

  const styles = useStyles();
  const test = 147;

  return (
    <NavLink to={{pathname: `/chat/${props.chat.chat_id}`, test: test}}>
      <Button className={styles.chat_bubble}>
        <Typography variant="subtitle1">Tenant ID: {props.chat.tenant_id}</Typography>
        <Typography variant="body2">Chat ID: {props.chat.chat_id}</Typography>        
      </Button>
    </NavLink>
  )
}

export default ChatCards;