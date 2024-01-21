import React, {useEffect, useState} from 'react';
import styles from "./style.module.css";
import GroupsHeading from "../GroupsHeading/GroupsHeading";
import GroupsList from "../GroupsList/GroupsList";
import CsrfFetch from "../../utils/CsrfFetch";
import CentralServerLinksProvider from "../../utils/CentralServerLinksProvider";
import AddGroupForm from "../AddGroupForm/AddGroupForm";
import RenameGroupForm from "../RenameGroupForm/RenameGroupForm";
import DeleteGroupForm from "../DeleteGroupForm/DeleteGroupForm";

const Groups = () => {
    const [selectedGroupsBuffer, setSelectedGroupsBuffer] = useState([]);
    const [groups, setGroups] = useState([]);
    const [isAddGroupFormHidden, setIsAddGroupFormHidden] = useState(true);
    const [isRenameGroupFormHidden, setIsRenameGroupFormHidden] = useState(true);
    const [isDeleteGroupFormHidden, setIsDeleteGroupFormHidden] = useState(true);

    useEffect(() => {
        void getGroups();
    }, []);

    async function getGroups() {
        let response = await CsrfFetch(await CentralServerLinksProvider.getLink('group-get'), {
            method: 'get'
        });
        setGroups((await response.json()).groups);
    }

    function addGroup() {
        setIsAddGroupFormHidden(false);
    }

    function renameGroup() {
        setIsRenameGroupFormHidden(false);
    }

    function deleteGroups() {
        setIsDeleteGroupFormHidden(false);
    }

    return (
        <div className={styles.groups}>
            <AddGroupForm isHidden={isAddGroupFormHidden} setIsHidden={setIsAddGroupFormHidden}/>
            <RenameGroupForm isHidden={isRenameGroupFormHidden} setIsHidden={setIsRenameGroupFormHidden} onRename={getGroups}
                group={groups[0]}/>
            <DeleteGroupForm isHidden={isDeleteGroupFormHidden} setIsHidden={setIsDeleteGroupFormHidden} onDelete={getGroups}
                selectedGroups={selectedGroupsBuffer}/>
            <GroupsHeading selectedGroups={selectedGroupsBuffer} addGroup={addGroup} renameGroup={renameGroup} deleteGroups={deleteGroups}/>
            <GroupsList items={groups} selectedGroupsBuffer={selectedGroupsBuffer} setSelectedGroupsBuffer={setSelectedGroupsBuffer}/>
        </div>
    );
};

export default Groups;