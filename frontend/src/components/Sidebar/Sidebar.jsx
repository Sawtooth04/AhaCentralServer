import React from 'react';
import style from './style.module.css'
import SidebarItem from "../SidebarItem/SidebarItem";

const Sidebar = () => {
    return (
        <div className={style.sidebar}>
            <SidebarItem text={'Главная'} link={'/'} src={'assets/icons/terminal-window-fill.png'}/>
            <SidebarItem text={'Файлы'} link={'/files'} src={'assets/icons/file-line.png'}/>
            <SidebarItem text={'Пользователи'} link={'/'} src={'assets/icons/contacts.png'}/>
            <SidebarItem text={'Группы'} link={'/groups'} src={'assets/icons/group_24px.png'}/>
            <SidebarItem text={'Центральный сервер'} link={'/'} src={'assets/icons/server__117.png'}/>
            <SidebarItem text={'Серверы хранения'} link={'/'} src={'assets/icons/server.png'}/>
            <SidebarItem text={'О системе'} link={'/'} src={'assets/icons/info_outline.png'}/>
        </div>
    );
};

export default Sidebar;