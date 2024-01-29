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
                    <Route path="/about" element={<React.StrictMode><About/></React.StrictMode>}/>
                    <Route path="/storage-servers" element={<React.StrictMode><StorageServers/></React.StrictMode>}/>
                    <Route path="/groups" element={<React.StrictMode><Groups/></React.StrictMode>}/>
                    <Route path="/files" element={<React.StrictMode><Files uploads={uploads} setUploads = {setUploads}/></React.StrictMode>}/>
                    <Route path="*" element={<React.StrictMode><Main/></React.StrictMode>}/>
                </Routes>
            </UploadsContext.Provider>
        </div>
    );
};

export default Content;