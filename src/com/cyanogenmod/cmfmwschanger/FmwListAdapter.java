/*
 * Copyright (C) 2010 Pixelpod INTERNATIONAL, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cyanogenmod.cmfmwschanger;

import com.cyanogenmod.cmfmwschanger.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Insert comments
 * 
 * @author Timothy Caraballo
 * @version 0.8
 */
public class FmwListAdapter extends ArrayAdapter<Object> {
    // Tag to use for logging
    private static final String TAG = "CM Fmws Adapter";
    LayoutInflater inflater;
    String[] fmwNames = null;
    String[] fmwPaths = null;

    /**
     * Class constructor.
     * 
     * @param context The owner of this adapter.
     * @param fmws   A <code>String[]</code> containing all the system fmw filenames.
     */
    FmwListAdapter(Activity context, String[] fmws) {
        super(context, R.layout.fmw_select, fmws);

        inflater = context.getLayoutInflater();
        fmwNames = new String[fmws.length];
        fmwPaths = new String[fmws.length];
        System.arraycopy(fmws, 0, this.fmwNames, 0, fmws.length);

        for (int i = 0; i < fmws.length; i++) {
            fmwPaths[i] = "/system/framework/" + fmws[i];
        }
    }

    /**
     * Returns the user-selected fmw paths
     * 
     * @return <code>String[]</code> of paths in same order as in the <code>ListView</code>
     */
    public String[] getPaths() {
        return fmwPaths;
    }
    
    /**
     * Returns the user-selected fmw path at the specified index
     * 
     * @param index index in list of fmws 
     * @return fmw path
     */
    public String getPathAt(int index) {
        return fmwPaths[index];
    }
    
    /**
     * Returns the system fmw filenames
     * 
     * @return <code>String[]</code> of installed fmw filenames in same order as in the
     *          <code>ListView</code>
     */
    public String[] getFmws() {
        return fmwNames;
    }

    /**
     * Sets a fmw at <code>position</code> to <code>path</code> to be applied later.
     * 
     * @param position Position in the <code>ListView</code> 
     * @param path     Full path of desired fmw.
     */
    public void setPathAt(int position, String path) {
        fmwPaths[position] = path;
        notifyDataSetChanged();
    }

    /**
     * Sets all fmws desired paths at once to be applied later.
     * 
     * @param paths <code>String[]</code> of paths to be applied in same order as in the
     *               <code>ListView</code>
     */
    public void setPaths(String[] paths) {
        if (paths.length != fmwPaths.length) {
            // TODO: throw exception?
            Log.i(TAG, "Not resetting paths");
        } else {
            System.arraycopy(paths, 0, fmwPaths, 0, paths.length);
            notifyDataSetChanged();
        }
        
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {    
            convertView = inflater.inflate(R.layout.fmw_select, null);
            
            holder = new ViewHolder();
            holder.fmw_name = (TextView) convertView.findViewById(R.id.fmw_name);
            holder.fmw_location = (TextView) convertView.findViewById(R.id.fmw_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.fmw_name.setText(fmwNames[position]);
        // don't display fmw path if it's the system fmw
        if (fmwPaths[position].equals("/system/framework/" + fmwNames[position])) {
            holder.fmw_location.setVisibility(View.GONE);
        } else {
            holder.fmw_location.setText(fmwPaths[position]);
            holder.fmw_location.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    
    /**
     * Class that holds frequently accessed references to reduce calls to <code>findViewById</code>.
     * 
     * @author Timothy Caraballo
     *
     */
    public static class ViewHolder {
        TextView fmw_name;
        TextView fmw_location;
    }
}
