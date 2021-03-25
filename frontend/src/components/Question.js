import React, { useContext, useEffect } from "react";

import { FaRegEdit } from "react-icons/fa";
import { Context } from "../Context";

function Question({ id, text, tenantId }) {
  //Context: state of modal (whether it is open or not)
  const { openQuestionModal } = useContext(Context);
  //Context: state of tenants to update checklist check
  const { tenantsState, updateFbChecklistChecked } = useContext(Context);
  //Coontext: state of displayedComments

  //function to handle change when checkbox is clicked
  const handleChange = () => {
    updateFbChecklistChecked(tenantId, id);
    console.log(id);
  };

  return (
    <div>
      <span>{text}</span>
      <input
        type="checkbox"
        // id={id}
        // name={`Check${id}`}
        onChange={() => {
          handleChange();
        }}
      />
      <FaRegEdit
        onClick={() => {
          openQuestionModal(id);
        }}
      />

      <div>Comment: </div>
    </div>
  );
}

export default Question;
