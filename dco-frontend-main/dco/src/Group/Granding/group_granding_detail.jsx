import React, { useState } from 'react';
import "./group_granding_detail.css";
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { coy } from 'react-syntax-highlighter/dist/esm/styles/prism';

const assignment1 = [
    {
        id: 1,
        title: "1차과제",
        content: '뒤에 있는 큰 수 찾기',
        grandResults: [
            {
                submitNo: 1,
                data: {
                    submitNo: 1,
                    submitDate: "2024-04-14",
                    language: "python",
                    result: { correctness: "정답", score: "100", maxScore: "100" },
                    code: 'print("hello world!")'
                },
            },
            {
                submitNo: 2,
                data: {
                    submitDate: "2024-03-14",
                    language: "java",
                    result: { correctness: "오답", score: "50", maxScore: "100" },
                    code:
                        `
                        public class Main {
                            public static void main(String[] args) {
                                int num1 = 10;
                                int num2 = 20;
                                int num3 = 30;

                                int largest = Math.max(num1, Math.max(num2, num3));

                                System.out.println("The largest number is: " + largest);
                            }
                        }
                        `
                }
            },
            {
                submitNo: 3,
                data: {
                    submitDate: "2024-02-14",
                    language: "python",
                    result: { correctness: "오답", score: "20", maxScore: "100" },
                    code: 'print("지우")\nprint("배고파")'
                }
            }
        ]
    }
];

const ViewCode = ({ code, language, show, handleClose }) => {
    if (!show) return null;

    return (
        <div className="modal display-block">
            <section className="modal-main">
                <SyntaxHighlighter language={language} style={coy}>
                    {code}
                </SyntaxHighlighter>
                <button onClick={handleClose}>Close</button>
            </section>
        </div>
    );
};

const GrandingDetail = () => {
    const [openModalIndex, setOpenModalIndex] = useState(null);

    const showCode = (index) => {
        setOpenModalIndex(index);
    };

    const hideCode = () => {
        setOpenModalIndex(null);
    };

    if (!assignment1) {
        return <div>제출결과를 찾을 수 없습니다.</div>;
    }

    const currentResult = openModalIndex !== null ? assignment1[0].grandResults[openModalIndex] : null;

    return (
        <div className="granding-container">
            <table>
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>제출일시</th>
                        <th>언어</th>
                        <th>채점 내역</th>
                        <th>Code</th>
                    </tr>
                </thead>
                <tbody>
                    {assignment1[0].grandResults.map((result, index) => (
                        <tr key={index}>
                            <td>{result.submitNo}</td>
                            <td>{result.data.submitDate}</td>
                            <td>{result.data.language}</td>
                            <td>
                                <p className="correctness-style"
                                    style={{backgroundColor: result.data.result.correctness === "정답" ? 'green' : 'red'}}
                                >
                                    {result.data.result.correctness}
                                </p>  &nbsp;
                                {result.data.result.score}/{result.data.result.maxScore}</td>
                            <td>
                                <button onClick={() => showCode(index)}>코드 보기</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {currentResult && (
                <ViewCode
                    code={currentResult.data.code}
                    language={currentResult.data.language}
                    show={openModalIndex !== null}
                    handleClose={hideCode}
                />
            )}
        </div>
    );
};

export default GrandingDetail;



