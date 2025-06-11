import React from 'react';
import "./group_submission_table.css";

const SubmissionTable=({notifications})=>{
    return(
        <table>
            <thead>
                <tr>
                    <th>점수</th>
                    <th>100</th>
                    <th>90</th>
                    <th>80</th>
                    <th>70</th>
                    <th>0</th>
                </tr>
            </thead>
            <tbody>
                {notifications.map((data)=>(
                    <tr key={data.id}>
                        <td></td>
                        <td>{data.score_100}</td>
                        <td>{data.score_90}</td>
                        <td>{data.score_80}</td>
                        <td>{data.score_70}</td>
                        <td>{data.score_0}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    )
}

export default SubmissionTable;