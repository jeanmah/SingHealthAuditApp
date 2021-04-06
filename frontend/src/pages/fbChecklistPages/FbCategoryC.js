import React, {useState} from "react";
import {fbChecklist} from "../../data";
import {NavLink} from "react-router-dom";

function FbCategoryC() {
  const fbChecklistCategory = "C";

  const filteredChecklistLines = fbChecklist.filter(
    (checklistLine) => checklistLine.category === fbChecklistCategory
  );

  return (
    <div className="category-head">
      <h2>FB Checklist Category C</h2>
      {filteredChecklistLines.map((checklistLine, index) => {
        const {id, category, text} = checklistLine;
        return (
          <article>
            <div>{text}</div>
          </article>
        )
      })}
      <NavLink exact to="/fbCategoryB"><button>Previous</button></NavLink>
      <NavLink exact to="/fbCategoryD"><button>Next</button></NavLink>
    </div>
  )
}

export default FbCategoryC;