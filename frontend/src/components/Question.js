import React, { useContext, useEffect } from "react";

import { FaRegEdit } from "react-icons/fa";
import { Context } from "../Context";

function Question({ fb_qn_id, requirement }) {
  //Context: state of modal (whether it is open or not)
  const {
    openQuestionModal,
    fbReportState,
    setFbReportState,
    fbChecklist,
  } = useContext(Context);
  //Context: state of tenants to update checklist check
  // const { tenantsState, updateFbChecklistChecked } = useContext(Context);
  //Coontext: state of displayedComments
  //function to handle change when checkbox is clicked

  const handleChange = (questionId) => {
    console.log(questionId);
    setFbReportState((prevState) => {
      return prevState.map((question) =>
        question.qn_id === questionId
          ? { ...question, status: !question.status }
          : question
      );
    });

    // if (fbReportState.length === fbChecklist.length) {
    //   console.log(fbReportState);
    // }
  };

  useEffect(() => {
    if (fbReportState.length === 96) {
      console.log(fbReportState);
    }
  }, [fbReportState]);

  return (
    <div>
      <span>{requirement}</span>
      <input
        type="checkbox"
        // id={id}
        // name={`Check${id}`}
        onChange={() => {
          handleChange(fb_qn_id);
        }}
      />
      <FaRegEdit
        onClick={() => {
          // openQuestionModal(fb_qn_id);
        }}
      />

      <div>Comment: </div>
    </div>
  );
}

export default Question;
