import React, {useEffect, useState} from 'react';
import styles from './style.module.css'
import Sidebar from "../Sidebar/Sidebar";
import {Route, Routes} from "react-router-dom";
import Main from "../Main/Main";
import Files from "../Files/Files";
import Groups from "../Groups/Groups";
import StorageServers from "../StorageServers/StorageServers";
import About from "../About/About";
import FilesUploads from "../FilesUploads/FilesUploads";
import UploadsContext from "../../contexts/UploadsContext";

const Content = () => {
    const [uploads, setUploads] = useState([]);

    useEffect(() => {
        console.log(uploads);
    }, [uploads]);

    return (
        <div className={styles.content}>
            <Sidebar/>
            <UploadsContext.Provider value={uploads}>
                <Routes>
                    <Route path="/uploads" element={<FilesUploads/>}/>
                    <Route path="/about" element={<About/>}/>
                    <Route path="/storage-servers" element={<StorageServers/>}/>
                    <Route path="/groups" element={<Groups/>}/>
                    <Route path="/files" element={<Files uploads={uploads} setUploads = {setUploads}/>}/>
                    <Route path="*" element={<Main/>}/>
                </Routes>
            </UploadsContext.Provider>
        </div>
    );
};

export default Content;