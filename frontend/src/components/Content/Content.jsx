import React from 'react';
import styles from './style.module.css'
import Sidebar from "../Sidebar/Sidebar";
import {Route, Routes} from "react-router-dom";
import Main from "../Main/Main";
import Files from "../Files/Files";

const Content = () => {
    return (
        <div className={styles.content}>
            <Sidebar/>
            <Routes>
                <Route path="/files" element={<Files/>}/>
                <Route path="*" element={<Main/>}/>
            </Routes>
        </div>
    );
};

export default Content;