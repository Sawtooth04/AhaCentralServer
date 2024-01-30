import React from 'react';
import style from './style.module.css'
import SidebarItem from "../SidebarItem/SidebarItem";

const Sidebar = () => {
    return (
        <div className={style.sidebar}>
            <SidebarItem text={'Главная'} link={'/'} src={'assets/icons/terminal-window-fill.png'}/>
            <SidebarItem text={'Файлы'} link={'/files'} src={'assets/icons/file-line.png'}/>
            <SidebarItem text={'Загрузки'} link={'/uploads'} src={'assets/icons/upload.png'}/>
            <SidebarItem text={'Пользователи'} link={'/customers'} src={'assets/icons/contacts.png'}/>
            <SidebarItem text={'Группы'} link={'/groups'} src={'assets/icons/group_24px.png'}/>
            <SidebarItem text={'Серверы хранения'} link={'/storage-servers'} src={'assets/icons/server.png'}/>
            <SidebarItem text={'О системе'} link={'/about'} src={'assets/icons/info_outline.png'}/>
        </div>
    );
};

export default Sidebar;