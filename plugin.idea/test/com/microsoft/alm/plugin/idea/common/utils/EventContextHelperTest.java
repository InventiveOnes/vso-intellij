// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.idea.common.utils;

import com.intellij.openapi.project.Project;
import com.microsoft.alm.plugin.events.ServerEvent;
import com.microsoft.alm.plugin.events.ServerEventManager;
import com.microsoft.alm.plugin.idea.IdeaAbstractTest;
import git4idea.repo.GitRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerEventManager.class})
public class EventContextHelperTest extends IdeaAbstractTest {

    @Mock
    public ServerEventManager serverEventManager;

    @Before
    public void setupLocalTests() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ServerEventManager.class);
        when(ServerEventManager.getInstance()).thenReturn(serverEventManager);
    }

    @Test
    public void testCreate() {
        final Map<String, Object> map = EventContextHelper.createContext("test");
        Assert.assertNotNull(map);
        Assert.assertEquals("test", map.get(EventContextHelper.CONTEXT_SENDER));

        final Map<String, Object> map2 = EventContextHelper.createContext(null);
        Assert.assertNotNull(map2);
        Assert.assertFalse(map2.containsKey(EventContextHelper.CONTEXT_SENDER));
    }

    @Test
    public void testSetGetSender() {
        String sender = "test";
        // Test the precondition that sender is not in the map
        final Map<String, Object> map = EventContextHelper.createContext(null);
        Assert.assertNotNull(map);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_SENDER));
        // Add sender and make sure it exists
        EventContextHelper.setSender(map, sender);
        Assert.assertEquals(sender, map.get(EventContextHelper.CONTEXT_SENDER));
        // Make sure that the getRepository method returns the exact same value
        String sender2 = EventContextHelper.getSender(map);
        Assert.assertEquals(sender, sender2);
        // Remove sender and make sure it is removed
        EventContextHelper.setSender(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_SENDER));
        // Repeat removal - make sure this doesn't change anything
        EventContextHelper.setSender(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_SENDER));
    }

    @Test
    public void testSetGetProject() {
        Project project = Mockito.mock(Project.class);
        // Test the precondition that project is not in the map
        final Map<String, Object> map = EventContextHelper.createContext(null);
        Assert.assertNotNull(map);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_PROJECT));
        // Add project and make sure it exists
        EventContextHelper.setProject(map, project);
        Assert.assertEquals(project, map.get(EventContextHelper.CONTEXT_PROJECT));
        // Make sure that the getProject method returns the exact same value
        Project project2 = EventContextHelper.getProject(map);
        Assert.assertEquals(project, project2);
        // Remove project and make sure it is removed
        EventContextHelper.setProject(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_PROJECT));
        // Repeat removal - make sure this doesn't change anything
        EventContextHelper.setProject(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_PROJECT));
    }

    @Test
    public void testSetGetRepository() {
        GitRepository repo = Mockito.mock(GitRepository.class);
        // Test the precondition that project is not in the map
        final Map<String, Object> map = EventContextHelper.createContext(null);
        Assert.assertNotNull(map);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_REPOSITORY));
        // Add project and make sure it exists
        EventContextHelper.setRepository(map, repo);
        Assert.assertEquals(repo, map.get(EventContextHelper.CONTEXT_REPOSITORY));
        // Make sure that the getRepository method returns the exact same value
        GitRepository repo2 = EventContextHelper.getRepository(map);
        Assert.assertEquals(repo, repo2);
        // Remove project and make sure it is removed
        EventContextHelper.setRepository(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_REPOSITORY));
        // Repeat removal - make sure this doesn't change anything
        EventContextHelper.setRepository(map, null);
        Assert.assertFalse(map.containsKey(EventContextHelper.CONTEXT_REPOSITORY));
    }

    @Test
    public void testIsProjectOpened() {
        Project project = Mockito.mock(Project.class);
        final Map<String, Object> map = EventContextHelper.createContext(EventContextHelper.SENDER_PROJECT_OPENED);
        // Make sure that false is sent until the project is set as well
        Assert.assertEquals(false, EventContextHelper.isProjectOpened(map));
        // make sure that true is returned when both sender and project are set
        EventContextHelper.setProject(map, project);
        Assert.assertEquals(true, EventContextHelper.isProjectOpened(map));
        // Make sure that if sender is missing we get a false result
        EventContextHelper.setSender(map, null);
        Assert.assertEquals(false, EventContextHelper.isProjectOpened(map));
        // Make sure that if sender is something else that we get a false result
        EventContextHelper.setSender(map, "test");
        Assert.assertEquals(false, EventContextHelper.isProjectOpened(map));
    }

    @Test
    public void testIsProjectClosing() {
        Project project = Mockito.mock(Project.class);
        final Map<String, Object> map = EventContextHelper.createContext(EventContextHelper.SENDER_PROJECT_CLOSING);
        // Make sure that false is sent until the project is set as well
        Assert.assertEquals(false, EventContextHelper.isProjectClosing(map));
        // make sure that true is returned when both sender and project are set
        EventContextHelper.setProject(map, project);
        Assert.assertEquals(true, EventContextHelper.isProjectClosing(map));
        // Make sure that if sender is missing we get a false result
        EventContextHelper.setSender(map, null);
        Assert.assertEquals(false, EventContextHelper.isProjectClosing(map));
        // Make sure that if sender is something else that we get a false result
        EventContextHelper.setSender(map, "test");
        Assert.assertEquals(false, EventContextHelper.isProjectClosing(map));
    }

    @Test
    public void testIsRepositoryChanged() {
        Project project = Mockito.mock(Project.class);
        final Map<String, Object> map = EventContextHelper.createContext(EventContextHelper.SENDER_REPO_CHANGED);
        // Make sure that false is sent until the project is set as well
        Assert.assertEquals(false, EventContextHelper.isRepositoryChanged(map));
        // make sure that true is returned when both sender and project are set
        EventContextHelper.setProject(map, project);
        Assert.assertEquals(true, EventContextHelper.isRepositoryChanged(map));
        // Make sure that if sender is missing we get a false result
        EventContextHelper.setSender(map, null);
        Assert.assertEquals(false, EventContextHelper.isRepositoryChanged(map));
        // Make sure that if sender is something else that we get a false result
        EventContextHelper.setSender(map, "test");
        Assert.assertEquals(false, EventContextHelper.isRepositoryChanged(map));
    }

    @Test
    public void testTriggerPullRequestChanged() {
        Project project = Mockito.mock(Project.class);
        EventContextHelper.triggerPullRequestChanged(EventContextHelper.SENDER_CREATE_PULL_REQUEST, project);
        verify(serverEventManager).triggerEvent(Matchers.eq(ServerEvent.PULL_REQUESTS_CHANGED), Matchers.anyMap());
    }

    @Test
    public void testTriggerWorkItemChanged() {
        Project project = Mockito.mock(Project.class);
        EventContextHelper.triggerWorkItemChanged(EventContextHelper.SENDER_ASSOCIATE_BRANCH, project);
        verify(serverEventManager).triggerEvent(Matchers.eq(ServerEvent.WORK_ITEMS_CHANGED), Matchers.anyMap());
    }
}
