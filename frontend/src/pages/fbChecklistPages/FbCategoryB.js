import React from "react";
import {fbChecklist} from "../../data";
import {NavLink} from "react-router-dom";
import FbCategoryA from "./FbCategoryA";

function FbCategoryB() {
  const fbChecklistCategory = "B";

  const filteredChecklistLines = fbChecklist.filter(
    (checklistLine) => checklistLine.category === fbChecklistCategory
  );

  return (
    <div className="category-head">
      <h2>FB Checklist Category B</h2>
      {filteredChecklistLines.map((checklistLine, index) => {
        const {id, category, text} = checklistLine;
        return (
          <article>
            <div>{text}</div>
          </article>
        )
      })}
      <NavLink exact to="/fbCategoryA"><button>Previous</button></NavLink>
      <NavLink exact to="/fbCategoryC"><button>Next</button></NavLink>
    </div>
  )
}

export default FbCategoryB;