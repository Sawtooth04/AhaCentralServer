import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import Button from "../UI/Button/Button";
import csrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import CsrfFetch from "../../utils/CsrfFetch";
import GroupFileRightsList from "../GroupFileRightsList/GroupFileRightsList";
import buildCsrfXhr from "../../utils/CsrfXhr";

const PutFileForm = ({ isHidden, setIsHidden, file, buildPath, uploads, setUploads }) => {
    const [groups, setGroups] = useState([]);
    const [fileRights, setFileRights] = useState([]);
    const [groupsFileRights, setGroupsFileRights] = useState({});

    useEffect(() => {
        if (!isHidden) {
            void getGroups();
            void getFileRights();
        }
    }, [isHidden]);

    async function getGroups() {
        let response = await CsrfFetch(await CentralServerLinksProvider.getLink('group-own-get'), {
            method: 'get'
        });
        setGroups((await response.json()).groups);
    }

    async function getFileRights() {
        let response = await CsrfFetch(await CentralServerLinksProvider.getLink('file-right-all-get'), {
            method: 'get'
        });
        setFileRights((await response.json()).fileRights);
    }

    async function postGroupsFileRights(path) {
        for (let groupID in groupsFileRights)
            for (let fileRightID of groupsFileRights[groupID])
                await csrfFetch(await CentralServerLinksProvider.getLink('group-file-right-post'), {
                    method: 'post',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        'groupFileRight': {
                            'groupID': groupID,
                            'fileRightID': fileRightID
                        },
                        'fileName': file.name,
                        'path': path
                    })
                });
    }

    async function putFile() {
        let formData = new FormData(), path = buildPath();

        formData.append('file', file);
        formData.append('path', `${path}`);
        setUploads([...uploads, {name: file.name, formData: formData, onSuccess: () => postGroupsFileRights(path)}]);
    }

    function isFileRightIncluded(groupID, fileRightID) {
        return typeof groupsFileRights[groupID] !== 'undefined' &&  groupsFileRights[groupID].includes(fileRightID);
    }

    function appendFileRight(group, fileRight) {
        if (typeof groupsFileRights[group.groupID] === 'undefined')
            groupsFileRights[group.groupID] = [];
        groupsFileRights[group.groupID].push(fileRight.fileRightID);
    }

    function deleteFileRight(group, fileRight) {
        groupsFileRights[group.groupID].splice(groupsFileRights[group.groupID].indexOf(fileRight.fileRightID), 1)
    }

    function onFileRightClick(group, fileRight) {
        let newGroupsFileRights;

        if (!isFileRightIncluded(group.groupID, fileRight.fileRightID))
            appendFileRight(group, fileRight);
        else
            deleteFileRight(group, fileRight);
        newGroupsFileRights = {...groupsFileRights};
        newGroupsFileRights[group.groupID] = [...newGroupsFileRights[group.groupID]];
        setGroupsFileRights(newGroupsFileRights);
    }

    return (
        <div className={styles.putFileForm}>
            <PopUpForm header={'Загрузка файла'} isHidden={isHidden}>
                <GroupFileRightsList groups={groups} fileRights={fileRights} onFileRightClick={onFileRightClick}
                    isFileRightIncluded={isFileRightIncluded}/>
                <Button text={'Загрузить'} onClick={putFile}/>
                <Button text={'Отменить'} onClick={() => setIsHidden(true)}/>
            </PopUpForm>
        </div>
    );
};

export default PutFileForm;