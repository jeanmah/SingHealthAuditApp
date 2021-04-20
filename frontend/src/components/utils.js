import React from "react";

export function getDate() {
  var today = new Date();
  var year = today.getFullYear();
  var month = addZero(today.getMonth()+1);
  var day = addZero(today.getDate());
  return year + "-" + month + "-" + day;
};

export function getDateAfterOneMonth() {
  var today = new Date();
  var year = today.getFullYear();
  var month = parseInt(addZero(today.getMonth()+2));
  var day = addZero(today.getDate());
  if (month>12) {
    month -= 12;
    year += 1;
  }
  return year + "-" + month + "-" + day;
}

export function addZero(number) {
  if (number < 10) return "0" + number;
  else return number;
};

export function getTime() {
  var today = new Date();
  var hours = addZero(today.getHours());
  var minutes = addZero(today.getMinutes());
  var seconds = addZero(today.getSeconds());
  return hours + ":" + minutes + ":" + seconds;
}