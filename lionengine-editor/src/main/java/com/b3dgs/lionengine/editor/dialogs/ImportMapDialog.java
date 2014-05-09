/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.editor.dialogs;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.b3dgs.lionengine.UtilityFile;
import com.b3dgs.lionengine.editor.Activator;
import com.b3dgs.lionengine.editor.project.Project;

/**
 * Represents the import map dialog.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class ImportMapDialog
        extends AbstractDialog
{
    /** Icon. */
    private static final Image ICON = Activator.getIcon("dialog", "import-map.png");

    /** Level rip location. */
    Text levelRipLocationText;
    /** Patterns location. */
    Text patternsLocationText;
    /** Level rip file. */
    String levelRip;
    /** Patterns directory. */
    String patternsDirectory;
    /** Found. */
    private boolean found;

    /**
     * Constructor.
     * 
     * @param parent The shell parent.
     */
    public ImportMapDialog(Shell parent)
    {
        super(parent, Messages.ImportMapDialog_Title, Messages.ImportMapDialog_HeaderTitle,
                Messages.ImportMapDialog_HeaderDesc, ImportMapDialog.ICON);
        createDialog();
    }

    /**
     * Get the level rip location.
     * 
     * @return The level rip location.
     */
    public String getLevelRipLocation()
    {
        return levelRip;
    }

    /**
     * Get the patterns location.
     * 
     * @return The patterns location.
     */
    public String getPatternsLocation()
    {
        return patternsDirectory;
    }

    /**
     * Check if import is found.
     * 
     * @return <code>true</code> if found, <code>false</code> else.
     */
    public boolean isFound()
    {
        return found;
    }

    /**
     * Update the tips label.
     */
    void updateTipsLabel()
    {
        tipsLabel.setVisible(false);
    }

    /**
     * Create the level rip location area.
     * 
     * @param content The content composite.
     */
    private void createLevelRipLocationArea(Composite content)
    {
        final Composite levelRipArea = new Composite(content, SWT.NONE);
        levelRipArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        levelRipArea.setLayout(new GridLayout(3, false));

        final Label locationLabel = new Label(levelRipArea, SWT.NONE);
        locationLabel.setText(Messages.ImportMapDialog_LevelRipLocation);

        levelRipLocationText = new Text(levelRipArea, SWT.BORDER);
        levelRipLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        levelRipLocationText.setEditable(false);

        final Button browse = new Button(levelRipArea, SWT.PUSH);
        final GridData browseData = new GridData();
        browseData.widthHint = 64;
        browse.setLayoutData(browseData);
        browse.setText(Messages.AbstractProjectDialog_Browse);
        browse.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                final FileDialog fileDialog = new FileDialog(dialog, SWT.APPLICATION_MODAL);
                final Project project = Project.getActive();
                fileDialog.setFilterPath(UtilityFile.getPath(project.getPath().getPath(), project.getResources()));
                fileDialog.setFilterNames(new String[]
                {
                    Messages.ImportMapDialog_FileFilter
                });
                fileDialog.setFilterExtensions(new String[]
                {
                    "*.bmp;*.png"
                });
                final String path = fileDialog.open();
                if (path != null)
                {
                    levelRipLocationText.setText(path);
                    levelRip = levelRipLocationText.getText();
                    updateTipsLabel();
                    finish.setEnabled(levelRip != null && patternsDirectory != null);
                }
            }
        });
    }

    /**
     * Create the patterns location area.
     * 
     * @param content The content composite.
     */
    private void createPatternsLocationArea(Composite content)
    {
        final Composite levelRipArea = new Composite(content, SWT.NONE);
        levelRipArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        levelRipArea.setLayout(new GridLayout(3, false));

        final Label locationLabel = new Label(levelRipArea, SWT.NONE);
        locationLabel.setText(Messages.ImportMapDialog_PatternsLocation);

        patternsLocationText = new Text(levelRipArea, SWT.BORDER);
        patternsLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        patternsLocationText.setEditable(false);

        final Button browse = new Button(levelRipArea, SWT.PUSH);
        final GridData browseData = new GridData();
        browseData.widthHint = 64;
        browse.setLayoutData(browseData);
        browse.setText(Messages.AbstractProjectDialog_Browse);
        browse.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent)
            {
                final DirectoryDialog directoryDialog = new DirectoryDialog(dialog, SWT.APPLICATION_MODAL);
                final Project project = Project.getActive();
                directoryDialog.setFilterPath(UtilityFile.getPath(project.getPath().getPath(), project.getResources()));
                final String path = directoryDialog.open();
                if (path != null)
                {
                    patternsLocationText.setText(path);
                    patternsDirectory = patternsLocationText.getText();
                    updateTipsLabel();
                    final File patterns = new File(patternsDirectory, "patterns.xml");
                    if (!patterns.isFile())
                    {
                        setTipsMessage(AbstractDialog.ICON_ERROR, Messages.ImportMapDialog_ErrorPatterns);
                    }
                    final boolean isValid = levelRip != null && patternsDirectory != null && patterns.isFile();
                    finish.setEnabled(isValid);
                }
            }
        });
    }

    /*
     * AbstractDialog
     */

    @Override
    protected void createContent(Composite content)
    {
        createLevelRipLocationArea(content);
        createPatternsLocationArea(content);
    }

    @Override
    protected void onFinish()
    {
        found = levelRip != null && patternsDirectory != null;
    }
}
