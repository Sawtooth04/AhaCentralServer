import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import PopUpForm from "../UI/PopUpForm/PopUpForm";
import GroupFileRightsList from "../GroupFileRightsList/GroupFileRightsList";
import Button from "../UI/Button/Button";
import csrfFetch from "../../utils/CsrfFetch";

const ChangeFilePermissionsForm = ({ isHidden, setIsHidden, file, buildPath, forbidden }) => {
    const [groups, setGroups] = useState([]);
    const [fileRights, setFileRights] = useState([]);
    const [postList, setPostList] = useState([]);
    const [deleteList, setDeleteList] = useState([]);
    const [existingGroupFileRights, setExistingGroupFileRights] = useState([]);

    useEffect(() => {
        async function getExistingGroupFileRights() {
            let url = `${await CentralServerLinksProvider.getLink('group-file-right-get')}`
                .replace('{path}', buildPath()).replace('{name}', file.name),
                response = await CsrfFetch(url, {
                    method: 'get'
                }),
                groupFileRights = (await response.json()).groupFileRights;
            setExistingGroupFileRights(groupFileRights);
            setPostList(groupFileRights);
        }
        
        if (!isHidden) {
            void getGroups();
            void getFileRights();
            void getExistingGroupFileRights();
        }
    }, [buildPath, file, isHidden]);

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

    function isFileRightIncluded(list, groupID, fileRightID) {
        return list?.findIndex(r => r.groupID === groupID && r.fileRightID === fileRightID) !== -1;
    }

    function removeFromPostList(groupID, fileRightID) {
        let index = postList.findIndex(r => r.groupID === groupID && r.fileRightID === fileRightID);

        if (index !== -1) {
            postList.splice(index, 1);
            setPostList([...postList]);
        }
        setDeleteList([...deleteList, {'groupID': groupID, 'fileRightID': fileRightID}]);
    }

    function removeFromDeleteList(groupID, fileRightID) {
        let index = deleteList.findIndex(r => r.groupID === groupID && r.fileRightID === fileRightID);

        if (index !== -1) {
            deleteList.splice(index, 1);
            setDeleteList([...deleteList]);
        }
        setPostList([...postList, {'groupID': groupID, 'fileRightID': fileRightID}]);
    }

    function onFileRightClick(group, fileRight) {
        if (isFileRightIncluded(postList, group.groupID, fileRight.fileRightID))
            removeFromPostList(group.groupID, fileRight.fileRightID);
        else
            removeFromDeleteList(group.groupID, fileRight.fileRightID);
    }

    async function deleteGroupsFileRights() {
        let path = buildPath(), response;

        for (let groupFileRight of deleteList) {
            response = await csrfFetch(await CentralServerLinksProvider.getLink('group-file-right-delete'), {
                method: 'delete',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    'groupFileRight': {
                        'groupID': groupFileRight.groupID,
                        'fileRightID': groupFileRight.fileRightID
                    },
                    'fileName': file.name,
                    'path': path
                })
            });
            if (response.status === 403) {
                forbidden();
                break;
            }
        }

    }

    async function postGroupsFileRights() {
        let path = buildPath(), response;

        for (let groupFileRight of postList)
            if (!isFileRightIncluded(existingGroupFileRights, groupFileRight.groupID, groupFileRight.fileRightID)) {
                response = await csrfFetch(await CentralServerLinksProvider.getLink('group-file-right-post'), {
                    method: 'post',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        'groupFileRight': {
                            'groupID': groupFileRight.groupID,
                            'fileRightID': groupFileRight.fileRightID
                        },
                        'fileName': file.name,
                        'path': path
                    })
                });
                if (response.status === 403) {
                    forbidden();
                    break;
                }
            }

    }

    async function onApply() {
        await postGroupsFileRights();
        await deleteGroupsFileRights();
        setIsHidden(true);
    }

    function onDecline() {
        setIsHidden(true);
        setGroups([]);
        setFileRights([]);
        setPostList([]);
        setDeleteList([]);
        setExistingGroupFileRights([]);
    }

    return (
        <div className={styles.changeFilePermissionsForm}>
            <PopUpForm header={'Изменение прав доступа'} isHidden={isHidden}>
                <GroupFileRightsList groups={groups} fileRights={fileRights} onFileRightClick={onFileRightClick}
                    isFileRightIncluded={(g, f) => {return isFileRightIncluded(postList, g, f)}}/>
                <Button text={'Применить'} onClick={onApply}/>
                <Button text={'Отменить'} onClick={onDecline}/>
            </PopUpForm>
        </div>
    );
};

export default ChangeFilePermissionsForm;