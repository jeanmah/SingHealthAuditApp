import React, { Component } from 'react'
import AuditChecklistService from '../../service/auditorServices/AuditChecklistService.js';
import { Link } from 'react-router-dom';

export const CHECKLISTSTARTCATEGORY = 'Professionalism'

class AuditChecklistComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            currentTenantId: this.props.match.params.tenantid,
            checklistType:this.props.match.params.checklistType,
            currentCategory:this.props.match.params.checklistcategory,
            questions: [],
            message: null
        }
        this.refreshChecklistCategory = this.refreshChecklistCategory.bind(this);
    }

    componentDidMount() {
        this.refreshChecklistCategory();
    }

    refreshChecklistCategory() {
        AuditChecklistService.getcategoryQuestions(this.state.currentTenantId, this.state.checklistType,this.state.currentCategory)
            .then(
                response => {
                    console.log(response.data);
                    this.setState({ questions: response.data})
                }
            )
            .catch(err => console.log("error " + err));

    }
    render() {
        console.log('render')
        return (
            <div className="container">
                <h3>{this.state.checklistType} {this.state.currentCategory}</h3>
                <form>
                    {(() => {
                        if (this.state.checklistType == "fbchecklist") {
                            return (
                                this.state.questions.map(
                                    question =>
                                    <div key = {question.fb_qn_id}>
                                        <label>{question.requirement}</label>
                                        <label>{question.sub_requirement}</label>
                                        <input type="radio" id={question.fb_qn_id + "true"} name = {question.fb_qn_id + "radioB"} value="true"/>
                                        <label htmlFor ={question.fb_qn_id + "true"}>true</label>
                                        <input type="radio" id={question.fb_qn_id + "false"}  name = {question.fb_qn_id + "radioB"} value="false"/>
                                        <label htmlFor ={question.fb_qn_id + "false"}>false</label>
                                    </div>
                                )
                            )
                        } else {
                            return (
                                this.state.questions.map(
                                    question =>
                                    <div key = {question.nfb_qn_id}>
                                        <label>{question.requirement}</label>
                                        <label>{question.sub_requirement}</label>
                                        <input type="radio" id={question.nfb_qn_id + "true"} name = {question.nfb_qn_id + "radioB"} value="true"/>
                                        <label for={question.nfb_qn_id + "true"}>true</label>
                                        <input type="radio" id={question.nfb_qn_id + "false"}  name = {question.nfb_qn_id + "radioB"} value="false"/>
                                        <label for={question.nfb_qn_id + "false"}>false</label>
                                    </div>
                                )                           
                            )
                        }
                    })()}

                </form>
            </div>
        )
    }
}

export default AuditChecklistComponent
