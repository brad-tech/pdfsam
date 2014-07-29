/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 25/lug/2014
 * Copyright 2013-2014 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.sejda.eventstudio.StaticStudio.eventStudio;

import java.io.File;
import java.io.OutputStream;

import javafx.scene.Parent;

import org.junit.Rule;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.pdfsam.test.ClearEventStudioRule;
import org.pdfsam.test.HitTestListener;
import org.pdfsam.ui.commons.OpenFileRequest;
import org.sejda.model.output.DirectoryTaskOutput;
import org.sejda.model.output.FileTaskOutput;
import org.sejda.model.output.StreamTaskOutput;

/**
 * @author Andrea Vacondio
 *
 */
public class OpenButtonTest extends GuiTest {

    @Rule
    public ClearEventStudioRule cearEventStudio = new ClearEventStudioRule();

    @Test
    public void openClick() {
        OpenButton victim = find(".pdfsam-footer-button");
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        FileTaskOutput output = mock(FileTaskOutput.class);
        when(output.getDestination()).thenReturn(file);
        TestListener listener = new TestListener(file);
        eventStudio().add(listener);
        victim.dispatch(output);
        click(victim);
        assertTrue(listener.isHit());
    }

    @Test
    public void fileDestination() {
        OpenButton victim = find(".pdfsam-footer-button");
        FileTaskOutput output = mock(FileTaskOutput.class);
        victim.dispatch(output);
        verify(output).getDestination();
    }

    @Test
    public void directoryDestination() {
        OpenButton victim = find(".pdfsam-footer-button");
        DirectoryTaskOutput output = mock(DirectoryTaskOutput.class);
        victim.dispatch(output);
        verify(output).getDestination();
    }

    @Test(expected = IllegalArgumentException.class)
    public void streamDestination() {
        OpenButton victim = find(".pdfsam-footer-button");
        OutputStream stream = mock(OutputStream.class);
        victim.dispatch(new StreamTaskOutput(stream));
    }

    @Override
    protected Parent getRootNode() {
        return new OpenButton();
    }

    private static class TestListener extends HitTestListener<OpenFileRequest> {
        private File destination;

        private TestListener(File destination) {
            this.destination = destination;
        }

        @Override
        public void onEvent(OpenFileRequest event) {
            super.onEvent(event);
            assertEquals(destination, event.getFile());
        }
    }

}